import React, { useState } from 'react';
import { useAuth } from '../contexts/AuthContext';
import { toast } from 'sonner';
import { HeartIcon, LockIcon, MailIcon, UserIcon, PhoneIcon, SparklesIcon, UsersIcon } from 'lucide-react';

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

  // Partner State (First Page Onboarding)
  const [partnerFirstName, setPartnerFirstName] = useState('');
  const [partnerLastName, setPartnerLastName] = useState('');
  const [partnerPhone, setPartnerPhone] = useState('');
  const [partnerEmail, setPartnerEmail] = useState('');

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
          partnerFirstName,
          partnerLastName,
          partnerPhone,
          partnerEmail,
        });
        toast.success('Account created! Partner invitation email sent via RabbitMQ with login credentials.');
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
    } catch {
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
      <div className="sm:mx-auto sm:w-full sm:max-w-lg text-center">
        <div className="inline-flex items-center justify-center mb-2">
          <img src="/logo.png" alt="Ubukwe Logo" className="h-24 w-auto object-contain" />
        </div>
        <h1 className="text-3xl font-serif font-bold text-[#1F1B1A] tracking-tight">
          Ubukwe
        </h1>
        <p className="mt-1 text-sm text-[#6E6663]">
          Centralized & Collaborative Wedding Planning Platform
        </p>
      </div>

      <div className="mt-8 sm:mx-auto sm:w-full sm:max-w-lg">
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
              <>
                <div className="mb-2 rounded-lg bg-[#581C26]/5 p-3 border border-[#581C26]/10 text-xs text-[#581C26]">
                  <p className="font-semibold flex items-center gap-1.5">
                    <HeartIcon className="w-4 h-4 fill-current text-[#581C26]" /> Step 1: Choose Your Role
                  </p>
                  <p className="mt-0.5 text-[11px] text-[#6E6663]">
                    Select whether you are registering as the Bride or Groom.
                  </p>
                </div>

                <div>
                  <label className="block text-xs font-medium text-[#1F1B1A] mb-1">
                    Your Role in the Wedding
                  </label>
                  <div className="grid grid-cols-2 gap-3">
                    <button
                      type="button"
                      onClick={() => setRole('ROLE_BRIDE')}
                      className={`py-2 px-3 rounded-lg border text-xs font-semibold flex items-center justify-center gap-2 transition-all ${
                        role === 'ROLE_BRIDE'
                          ? 'border-[#581C26] bg-[#581C26] text-white shadow-sm'
                          : 'border-[#D5CBC0] bg-white text-[#1F1B1A] hover:bg-[#FBF9F5]'
                      }`}
                    >
                      <HeartIcon className="w-3.5 h-3.5" /> Bride
                    </button>
                    <button
                      type="button"
                      onClick={() => setRole('ROLE_GROOM')}
                      className={`py-2 px-3 rounded-lg border text-xs font-semibold flex items-center justify-center gap-2 transition-all ${
                        role === 'ROLE_GROOM'
                          ? 'border-[#8A5A00] bg-[#8A5A00] text-white shadow-sm'
                          : 'border-[#D5CBC0] bg-white text-[#1F1B1A] hover:bg-[#FBF9F5]'
                      }`}
                    >
                      <UserIcon className="w-3.5 h-3.5" /> Groom
                    </button>
                  </div>
                </div>

                <div className="grid grid-cols-2 gap-3">
                  <div>
                    <label className="block text-xs font-medium text-[#1F1B1A] mb-1">
                      Your First Name
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
                      Your Last Name
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
              </>
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
                    Your Phone Number
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

                {/* Partner Details Section (First Setup Page) */}
                <div className="mt-4 border-t border-[#E9E2D8] pt-4">
                  <div className="mb-3 flex items-center gap-2 rounded-lg bg-gold-50 p-2.5 border border-gold-200 text-xs text-gold-900">
                    <UsersIcon className="w-4 h-4 text-gold-700 shrink-0" />
                    <div>
                      <p className="font-semibold text-ink">Partner Setup (Step 2)</p>
                      <p className="text-[11px] text-ink-500">
                        Enter your partner’s name, phone, and email. Your partner will receive an invitation email with credentials and have full authority to invite family support accounts.
                      </p>
                    </div>
                  </div>

                  <div className="space-y-3">
                    <div className="grid grid-cols-2 gap-3">
                      <div>
                        <label className="block text-xs font-medium text-[#1F1B1A] mb-1">
                          Partner First Name
                        </label>
                        <input
                          type="text"
                          required
                          placeholder="e.g. Jean"
                          value={partnerFirstName}
                          onChange={(e) => setPartnerFirstName(e.target.value)}
                          className="w-full px-3 py-2 border border-[#D5CBC0] rounded-lg text-sm text-[#1F1B1A] focus:outline-none focus:ring-2 focus:ring-[#581C26]"
                        />
                      </div>
                      <div>
                        <label className="block text-xs font-medium text-[#1F1B1A] mb-1">
                          Partner Last Name
                        </label>
                        <input
                          type="text"
                          required
                          placeholder="e.g. Paul"
                          value={partnerLastName}
                          onChange={(e) => setPartnerLastName(e.target.value)}
                          className="w-full px-3 py-2 border border-[#D5CBC0] rounded-lg text-sm text-[#1F1B1A] focus:outline-none focus:ring-2 focus:ring-[#581C26]"
                        />
                      </div>
                    </div>

                    <div>
                      <label className="block text-xs font-medium text-[#1F1B1A] mb-1">
                        Partner Email Address
                      </label>
                      <div className="relative">
                        <MailIcon className="w-4 h-4 absolute left-3 top-3 text-[#A19895]" />
                        <input
                          type="email"
                          required
                          placeholder="partner@example.rw"
                          value={partnerEmail}
                          onChange={(e) => setPartnerEmail(e.target.value)}
                          className="w-full pl-9 pr-3 py-2 border border-[#D5CBC0] rounded-lg text-sm text-[#1F1B1A] focus:outline-none focus:ring-2 focus:ring-[#581C26]"
                        />
                      </div>
                    </div>

                    <div>
                      <label className="block text-xs font-medium text-[#1F1B1A] mb-1">
                        Partner Phone Number
                      </label>
                      <div className="relative">
                        <PhoneIcon className="w-4 h-4 absolute left-3 top-3 text-[#A19895]" />
                        <input
                          type="tel"
                          placeholder="+250 788 111 222"
                          value={partnerPhone}
                          onChange={(e) => setPartnerPhone(e.target.value)}
                          className="w-full pl-9 pr-3 py-2 border border-[#D5CBC0] rounded-lg text-sm text-[#1F1B1A] focus:outline-none focus:ring-2 focus:ring-[#581C26]"
                        />
                      </div>
                    </div>
                  </div>
                </div>
              </>
            )}

            <button
              type="submit"
              disabled={isLoading}
              className="w-full mt-4 py-2.5 px-4 border border-transparent rounded-lg shadow-sm text-sm font-semibold text-white bg-[#581C26] hover:bg-[#43151D] focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-[#581C26] disabled:opacity-50 transition-colors"
            >
              {isLoading ? 'Processing...' : isRegisterMode ? 'Register & Invite Partner' : 'Sign In'}
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
