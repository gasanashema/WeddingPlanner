import React, { useState } from 'react';
import { X, KeyRound, UserPlus } from 'lucide-react';
import { weddingApi, WeddingDto } from '../../api/weddingApi';

interface JoinWeddingModalProps {
  isOpen: boolean;
  onClose: () => void;
  onSuccess: (wedding: WeddingDto) => void;
}

export function JoinWeddingModal({ isOpen, onClose, onSuccess }: JoinWeddingModalProps) {
  const [joinCode, setJoinCode] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  if (!isOpen) return null;

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!joinCode.trim()) return;

    setError(null);
    setLoading(true);

    try {
      const res = await weddingApi.joinWedding({
        joinCode: joinCode.trim(),
      });

      if (res.success && res.data) {
        onSuccess(res.data);
        onClose();
      } else {
        setError(res.message || 'Failed to join wedding');
      }
    } catch (err: any) {
      setError(err.message || 'Invalid join code or support account limit reached');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/60 backdrop-blur-sm p-4">
      <div className="relative w-full max-w-md rounded-2xl bg-slate-900 border border-indigo-500/20 shadow-2xl p-6 text-slate-100">
        <button
          onClick={onClose}
          className="absolute top-4 right-4 text-slate-400 hover:text-white transition-colors"
        >
          <X className="w-5 h-5" />
        </button>

        <div className="flex items-center gap-3 mb-6">
          <div className="p-3 rounded-xl bg-gradient-to-br from-indigo-500/20 to-purple-500/20 border border-indigo-500/30">
            <KeyRound className="w-6 h-6 text-indigo-400" />
          </div>
          <div>
            <h2 className="text-xl font-bold text-slate-100">Join Existing Wedding</h2>
            <p className="text-xs text-slate-400">Enter Partner Code (P-...) or Family Code (F-...)</p>
          </div>
        </div>

        {error && (
          <div className="mb-4 p-3 rounded-xl bg-rose-500/10 border border-rose-500/30 text-rose-300 text-sm">
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-xs font-semibold text-slate-300 mb-1">
              Join Code
            </label>
            <input
              type="text"
              required
              placeholder="e.g. P-A1B2C3D4 or F-E5F6G7H8"
              value={joinCode}
              onChange={(e) => setJoinCode(e.target.value)}
              className="w-full px-4 py-2.5 rounded-xl bg-slate-800/80 border border-slate-700 text-slate-100 placeholder-slate-500 focus:outline-none focus:border-indigo-500 text-sm tracking-wider uppercase font-mono"
            />
          </div>

          <div className="p-3 rounded-xl bg-slate-800/50 border border-slate-700/50 text-xs text-slate-400 space-y-1">
            <p>• <strong>Partner Code (P-...):</strong> Connects bride and groom accounts.</p>
            <p>• <strong>Family Code (F-...):</strong> Connects family support accounts (Max 2 per side).</p>
          </div>

          <div className="pt-4 flex gap-3">
            <button
              type="button"
              onClick={onClose}
              className="flex-1 py-2.5 rounded-xl border border-slate-700 text-slate-300 hover:bg-slate-800 text-sm font-semibold transition-colors"
            >
              Cancel
            </button>
            <button
              type="submit"
              disabled={loading}
              className="flex-1 py-2.5 rounded-xl bg-gradient-to-r from-indigo-500 to-purple-600 hover:from-indigo-600 hover:to-purple-700 text-white text-sm font-semibold shadow-lg shadow-indigo-500/25 flex items-center justify-center gap-2 transition-all"
            >
              {loading ? (
                <span>Joining...</span>
              ) : (
                <>
                  <UserPlus className="w-4 h-4" />
                  <span>Join Wedding</span>
                </>
              )}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
