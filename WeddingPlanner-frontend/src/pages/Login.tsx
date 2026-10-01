import React, { useState } from 'react';
import { useAuth } from '../contexts/AuthContext';
import { toast } from 'sonner';
import { HeartIcon, LockIcon, MailIcon, UserIcon, PhoneIcon, SparklesIcon } from 'lucide-react';

export function Login() {
  const { login, register } = useAuth();
  const [isRegisterMode, setIsRegisterMode] = useState(false);
  const [isLoading, setIsLoading] = useState(false);

  // Form State
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [phoneNumber, setPhoneNumber] = useState('');
  const [role, setRole] = useState('ROLE_BRIDE');

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsLoading(true);

    try {
      if (isRegisterMode) {
        await register({
          firstName,
          lastName,
          email,
          password,
          phoneNumber,
          role,
        });
        toast.success('Account created successfully! Welcome to WedPlan.');
      } else {
        await login({ email, password });
        toast.success('Signed in successfully!');
      }
    } catch (err: any) {
      toast.error(err.message || 'Authentication failed. Please check your credentials.');
    } finally {
      setIsLoading(false);
    }
  };

  const handleQuickDemoLogin = async (demoEmail: string, demoRole: string, name: string) => {
    setIsLoading(true);
    try {
      await login({ email: demoEmail, password: 'Password123!' });
      toast.success(`Signed in as ${name}`);
    } catch (err) {
      // If demo account doesn't exist in backend yet, automatically register it!
      try {
        await register({
          firstName: name.split(' ')[0],
          lastName: name.split(' ')[1] || 'User',
          email: demoEmail,
          password: 'Password123!',
          phoneNumber: '+250788000000',
          role: demoRole,
        });
        toast.success(`Demo account created and signed in as ${name}`);
      } catch (regErr: any) {
        toast.error(regErr.message || 'Failed to initialize demo account');
      }
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-[#FBF9F5] flex flex-col justify-center py-12 sm:px-6 lg:px-8 font-sans">
      <div className="sm:mx-auto sm:w-full sm:max-w-md text-center">
        <div className="inline-flex items-center justify-center w-14 h-14 rounded-full bg-[#581C26] text-[#C5A059] shadow-md mb-3">
          <HeartIcon className="w-7 h-7 fill-[#C5A059]" />
        </div>
        <h1 className="text-3xl font-serif font-bold text-[#1F1B1A] tracking-tight">
          WedPlan Rwanda
        </h1>
        <p className="mt-2 text-sm text-[#6E6663]">
          Centralized & Collaborative Wedding Planning Platform
        </p>
      </div>

      <div className="mt-8 sm:mx-auto sm:w-full sm:max-w-md">
        <div className="bg-white py-8 px-6 shadow-xl rounded-xl border border-[#E9E2D8] sm:px-10">
          <div className="flex border-b border-[#E9E2D8] mb-6">
            <button
              type="button"
              className={`flex-1 pb-3 text-sm font-semibold text-center transition-colors border-b-2 ${
                !isRegisterMode
                  ? 'border-[#581C26] text-[#581C26]'
                  : 'border-transparent text-[#6E6663] hover:text-[#1F1B1A]'
              }`}
              onClick={() => setIsRegisterMode(false)}
            >
              Sign In
            </button>
            <button
              type="button"
              className={`flex-1 pb-3 text-sm font-semibold text-center transition-colors border-b-2 ${
                isRegisterMode
                  ? 'border-[#581C26] text-[#581C26]'
                  : 'border-transparent text-[#6E6663] hover:text-[#1F1B1A]'
              }`}
              onClick={() => setIsRegisterMode(true)}
            >
              Register Account
            </button>
          </div>

          <form onSubmit={handleSubmit} className="space-y-4">
            {isRegisterMode && (
              <div className="grid grid-cols-2 gap-3">
                <div>
                  <label className="block text-xs font-medium text-[#1F1B1A] mb-1">
                    First Name
                  </label>
                  <div className="relative">
                    <UserIcon className="w-4 h-4 absolute left-3 top-3 text-[#A19895]" />
                    <input
                      type="text"
                      required
                      placeholder="e.g. Keza"
                      value={firstName}
                      onChange={(e) => setFirstName(e.target.value)}
                      className="w-full pl-9 pr-3 py-2 border border-[#D5CBC0] rounded-lg text-sm text-[#1F1B1A] focus:outline-none focus:ring-2 focus:ring-[#581C26]"
                    />
                  </div>
                </div>
                <div>
                  <label className="block text-xs font-medium text-[#1F1B1A] mb-1">
                    Last Name
                  </label>
                  <input
                    type="text"
                    required
                    placeholder="e.g. Divine"
                    value={lastName}
                    onChange={(e) => setLastName(e.target.value)}
                    className="w-full px-3 py-2 border border-[#D5CBC0] rounded-lg text-sm text-[#1F1B1A] focus:outline-none focus:ring-2 focus:ring-[#581C26]"
                  />
                </div>
              </div>
            )}

            <div>
              <label className="block text-xs font-medium text-[#1F1B1A] mb-1">
                Email Address
              </label>
              <div className="relative">
                <MailIcon className="w-4 h-4 absolute left-3 top-3 text-[#A19895]" />
                <input
                  type="email"
                  required
                  placeholder="name@example.rw"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  className="w-full pl-9 pr-3 py-2 border border-[#D5CBC0] rounded-lg text-sm text-[#1F1B1A] focus:outline-none focus:ring-2 focus:ring-[#581C26]"
                />
              </div>
            </div>

            <div>
              <label className="block text-xs font-medium text-[#1F1B1A] mb-1">
                Password
              </label>
              <div className="relative">
                <LockIcon className="w-4 h-4 absolute left-3 top-3 text-[#A19895]" />
                <input
                  type="password"
                  required
                  placeholder="••••••••"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  className="w-full pl-9 pr-3 py-2 border border-[#D5CBC0] rounded-lg text-sm text-[#1F1B1A] focus:outline-none focus:ring-2 focus:ring-[#581C26]"
                />
              </div>
            </div>

            {isRegisterMode && (
              <>
                <div>
                  <label className="block text-xs font-medium text-[#1F1B1A] mb-1">
                    Phone Number (Optional)
                  </label>
                  <div className="relative">
                    <PhoneIcon className="w-4 h-4 absolute left-3 top-3 text-[#A19895]" />
                    <input
                      type="tel"
                      placeholder="+250 788 000 000"
                      value={phoneNumber}
                      onChange={(e) => setPhoneNumber(e.target.value)}
                      className="w-full pl-9 pr-3 py-2 border border-[#D5CBC0] rounded-lg text-sm text-[#1F1B1A] focus:outline-none focus:ring-2 focus:ring-[#581C26]"
                    />
                  </div>
                </div>

                <div>
                  <label className="block text-xs font-medium text-[#1F1B1A] mb-1">
                    Your Wedding Role
                  </label>
                  <select
                    value={role}
                    onChange={(e) => setRole(e.target.value)}
                    className="w-full px-3 py-2 border border-[#D5CBC0] rounded-lg text-sm text-[#1F1B1A] bg-white focus:outline-none focus:ring-2 focus:ring-[#581C26]"
                  >
                    <option value="ROLE_BRIDE">Bride (Bride-side + Shared)</option>
                    <option value="ROLE_GROOM">Groom (Groom-side + Shared)</option>
                    <option value="ROLE_BRIDE_FAMILY_SUPPORT">Bride Family Support</option>
                    <option value="ROLE_GROOM_FAMILY_SUPPORT">Groom Family Support</option>
                    <option value="ROLE_GUEST">Guest</option>
                  </select>
                </div>
              </>
            )}

            <button
              type="submit"
              disabled={isLoading}
              className="w-full mt-4 py-2.5 px-4 border border-transparent rounded-lg shadow-sm text-sm font-semibold text-white bg-[#581C26] hover:bg-[#43151D] focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-[#581C26] disabled:opacity-50 transition-colors"
            >
              {isLoading ? 'Processing...' : isRegisterMode ? 'Create Account' : 'Sign In'}
            </button>
          </form>

          {/* Quick Demo Login Presets */}
          <div className="mt-8 border-t border-[#E9E2D8] pt-5">
            <p className="text-xs font-semibold text-[#6E6663] text-center mb-3 flex items-center justify-center gap-1.5">
              <SparklesIcon className="w-3.5 h-3.5 text-[#C5A059]" /> Quick Demo Accounts
            </p>
            <div className="grid grid-cols-2 gap-2 text-xs">
              <button
                type="button"
                onClick={() => handleQuickDemoLogin('aline@wedding.rw', 'ROLE_BRIDE', 'Aline (Bride)')}
                className="py-2 px-3 border border-[#D5CBC0] rounded-lg text-left hover:bg-[#FBF9F5] transition-colors"
              >
                <span className="font-semibold block text-[#581C26]">Bride (Aline)</span>
                <span className="text-[10px] text-[#6E6663]">Bride Private + Shared</span>
              </button>

              <button
                type="button"
                onClick={() => handleQuickDemoLogin('shema@wedding.rw', 'ROLE_GROOM', 'Shema (Groom)')}
                className="py-2 px-3 border border-[#D5CBC0] rounded-lg text-left hover:bg-[#FBF9F5] transition-colors"
              >
                <span className="font-semibold block text-[#8A5A00]">Groom (Shema)</span>
                <span className="text-[10px] text-[#6E6663]">Groom Private + Shared</span>
              </button>

              <button
                type="button"
                onClick={() => handleQuickDemoLogin('bride.support@wedding.rw', 'ROLE_BRIDE_FAMILY_SUPPORT', 'Mama Aline (Support)')}
                className="py-2 px-3 border border-[#D5CBC0] rounded-lg text-left hover:bg-[#FBF9F5] transition-colors"
              >
                <span className="font-semibold block text-[#581C26]">Bride Support</span>
                <span className="text-[10px] text-[#6E6663]">Bride-side records</span>
              </button>

              <button
                type="button"
                onClick={() => handleQuickDemoLogin('groom.support@wedding.rw', 'ROLE_GROOM_FAMILY_SUPPORT', 'Papa Shema (Support)')}
                className="py-2 px-3 border border-[#D5CBC0] rounded-lg text-left hover:bg-[#FBF9F5] transition-colors"
              >
                <span className="font-semibold block text-[#8A5A00]">Groom Support</span>
                <span className="text-[10px] text-[#6E6663]">Groom-side records</span>
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
