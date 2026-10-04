import React, { useState } from 'react';
import { authApi } from '../../api/authApi';
import { toast } from 'sonner';
import { LockIcon, KeyIcon } from 'lucide-react';
import { useAuth } from '../../contexts/AuthContext';

export function ChangePasswordModal({ isOpen, onClose }: { isOpen: boolean; onClose: () => void }) {
  const { user } = useAuth();
  const [currentPassword, setCurrentPassword] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [isLoading, setIsLoading] = useState(false);

  if (!isOpen) return null;

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (newPassword !== confirmPassword) {
      toast.error('New passwords do not match!');
      return;
    }
    if (newPassword.length < 6) {
      toast.error('Password must be at least 6 characters long.');
      return;
    }

    setIsLoading(true);
    try {
      await authApi.changePassword({ currentPassword, newPassword });
      toast.success('Password updated successfully!');
      if (user) {
        user.mustChangePassword = false;
      }
      onClose();
    } catch (err: any) {
      toast.error(err.message || 'Failed to update password.');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50 p-4">
      <div className="w-full max-w-md rounded-xl border border-[#E9E2D8] bg-white p-6 shadow-2xl">
        <div className="flex items-center gap-3 border-b border-[#E9E2D8] pb-4">
          <div className="flex h-10 w-10 items-center justify-center rounded-full bg-gold-100 text-gold-700">
            <KeyIcon className="h-5 w-5" />
          </div>
          <div>
            <h3 className="font-serif text-lg font-semibold text-[#1F1B1A]">Update Your Password</h3>
            <p className="text-xs text-[#6E6663]">
              You logged in with a temporary password. Please set a new secure password.
            </p>
          </div>
        </div>

        <form onSubmit={handleSubmit} className="mt-4 space-y-4">
          <div>
            <label className="block text-xs font-medium text-[#1F1B1A] mb-1">
              Current Temporary Password
            </label>
            <div className="relative">
              <LockIcon className="w-4 h-4 absolute left-3 top-3 text-[#A19895]" />
              <input
                type="password"
                required
                placeholder="Partner123!"
                value={currentPassword}
                onChange={(e) => setCurrentPassword(e.target.value)}
                className="w-full pl-9 pr-3 py-2 border border-[#D5CBC0] rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-[#581C26]"
              />
            </div>
          </div>

          <div>
            <label className="block text-xs font-medium text-[#1F1B1A] mb-1">
              New Password
            </label>
            <div className="relative">
              <LockIcon className="w-4 h-4 absolute left-3 top-3 text-[#A19895]" />
              <input
                type="password"
                required
                placeholder="Enter new password"
                value={newPassword}
                onChange={(e) => setNewPassword(e.target.value)}
                className="w-full pl-9 pr-3 py-2 border border-[#D5CBC0] rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-[#581C26]"
              />
            </div>
          </div>

          <div>
            <label className="block text-xs font-medium text-[#1F1B1A] mb-1">
              Confirm New Password
            </label>
            <div className="relative">
              <LockIcon className="w-4 h-4 absolute left-3 top-3 text-[#A19895]" />
              <input
                type="password"
                required
                placeholder="Confirm new password"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
                className="w-full pl-9 pr-3 py-2 border border-[#D5CBC0] rounded-lg text-sm focus:outline-none focus:ring-2 focus:ring-[#581C26]"
              />
            </div>
          </div>

          <button
            type="submit"
            disabled={isLoading}
            className="w-full mt-2 py-2.5 px-4 rounded-lg text-sm font-semibold text-white bg-[#581C26] hover:bg-[#43151D] disabled:opacity-50 transition-colors"
          >
            {isLoading ? 'Updating...' : 'Save New Password'}
          </button>
        </form>
      </div>
    </div>
  );
}
