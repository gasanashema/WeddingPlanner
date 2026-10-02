import React, { useState } from 'react';
import { X, Sparkles, Heart, DollarSign } from 'lucide-react';
import { weddingApi, WeddingDto } from '../../api/weddingApi';

interface CreateWeddingModalProps {
  isOpen: boolean;
  onClose: () => void;
  onSuccess: (wedding: WeddingDto) => void;
}

export function CreateWeddingModal({ isOpen, onClose, onSuccess }: CreateWeddingModalProps) {
  const [title, setTitle] = useState('');
  const [targetBudget, setTargetBudget] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  if (!isOpen) return null;

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);
    setLoading(true);

    try {
      const res = await weddingApi.createWedding({
        title: title.trim() || 'Our Ubukwe Wedding',
        targetBudget: targetBudget ? parseFloat(targetBudget) : undefined,
      });

      if (res.success && res.data) {
        onSuccess(res.data);
        onClose();
      } else {
        setError(res.message || 'Failed to create wedding');
      }
    } catch (err: any) {
      setError(err.message || 'An unexpected error occurred');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/60 backdrop-blur-sm p-4">
      <div className="relative w-full max-w-md rounded-2xl bg-slate-900 border border-amber-500/20 shadow-2xl p-6 text-slate-100">
        <button
          onClick={onClose}
          className="absolute top-4 right-4 text-slate-400 hover:text-white transition-colors"
        >
          <X className="w-5 h-5" />
        </button>

        <div className="flex items-center gap-3 mb-6">
          <div className="p-3 rounded-xl bg-gradient-to-br from-amber-500/20 to-rose-500/20 border border-amber-500/30">
            <Heart className="w-6 h-6 text-amber-400" />
          </div>
          <div>
            <h2 className="text-xl font-bold text-slate-100">Create New Wedding</h2>
            <p className="text-xs text-slate-400">Initialize your Ubukwe wedding workspace</p>
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
              Wedding Title
            </label>
            <input
              type="text"
              required
              placeholder="e.g., Divine & Jean Royal Ubukwe"
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              className="w-full px-4 py-2.5 rounded-xl bg-slate-800/80 border border-slate-700 text-slate-100 placeholder-slate-500 focus:outline-none focus:border-amber-500 text-sm"
            />
          </div>

          <div>
            <label className="block text-xs font-semibold text-slate-300 mb-1">
              Target Budget (RWF)
            </label>
            <div className="relative">
              <span className="absolute left-3.5 top-2.5 text-slate-500 text-xs font-bold">RWF</span>
              <input
                type="number"
                min="0"
                step="10000"
                placeholder="15,000,000"
                value={targetBudget}
                onChange={(e) => setTargetBudget(e.target.value)}
                className="w-full pl-12 pr-4 py-2.5 rounded-xl bg-slate-800/80 border border-slate-700 text-slate-100 placeholder-slate-500 focus:outline-none focus:border-amber-500 text-sm"
              />
            </div>
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
              className="flex-1 py-2.5 rounded-xl bg-gradient-to-r from-amber-500 to-rose-600 hover:from-amber-600 hover:to-rose-700 text-white text-sm font-semibold shadow-lg shadow-amber-500/25 flex items-center justify-center gap-2 transition-all"
            >
              {loading ? (
                <span>Creating...</span>
              ) : (
                <>
                  <Sparkles className="w-4 h-4" />
                  <span>Create Wedding</span>
                </>
              )}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
