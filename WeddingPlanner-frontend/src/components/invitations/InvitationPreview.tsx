import React from 'react';
import { wedding } from '../../data/wedding';
import { formatDate } from '../../utils/format';

export function InvitationPreview() {
  return (
    <div className="mx-auto w-full max-w-sm rounded-md bg-surface p-3 shadow-pop">
      <div className="rounded-sm border border-gold-300 px-6 py-10 text-center">
        <p className="text-[11px] font-medium tracking-[0.2em] text-gold-700">MWATUMIWE MU BUKWE BWACU</p>
        <p className="mt-6 text-xs text-ink-500">Together with their families</p>
        <p className="mt-3 font-serif text-3xl font-semibold text-wine-800">Aline Uwase</p>
        <p className="my-1 font-serif text-lg italic text-gold-600">&amp;</p>
        <p className="font-serif text-3xl font-semibold text-wine-800">Shema Mugisha</p>
        <p className="mx-auto mt-5 max-w-[240px] text-sm leading-relaxed text-ink-600">request the pleasure of your company as they celebrate their marriage</p>
        <div className="mx-auto my-6 h-px w-16 bg-gold-300" />
        <p className="font-serif text-lg text-ink">{formatDate(wedding.date, 'EEEE, d MMMM yyyy')}</p>
        <div className="mt-4 space-y-1 text-xs text-ink-600">
          <p>Ceremony · 11:00 · {wedding.church}</p>
          <p>Reception · 15:00 · {wedding.venue}</p>
        </div>
        <p className="mt-6 text-[11px] text-ink-500">Kindly respond by {formatDate(wedding.rsvpBy, 'd MMMM yyyy')}</p>
      </div>
    </div>);

}