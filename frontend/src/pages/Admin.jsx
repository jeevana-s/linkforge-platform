import { useEffect, useState } from 'react';
import api from '../api/client';
import toast from 'react-hot-toast';

export default function Admin(){
  const [users,setUsers]=useState([]); const [urls,setUrls]=useState([]); const [stats,setStats]=useState(null);
  const load = async () => {
    const [u,l,s] = await Promise.all([api.get('/api/admin/users'), api.get('/api/admin/urls'), api.get('/api/admin/stats')]);
    setUsers(u.data.content||[]); setUrls(l.data.content||[]); setStats(s.data);
  };
  useEffect(()=>{ load().catch(()=>toast.error('Failed')); },[]);
  const block = async id => { await api.post(`/api/admin/urls/${id}/block`); toast.success('Blocked'); load(); };
  const disable = async id => { await api.post(`/api/admin/users/${id}/disable`); toast.success('Disabled'); load(); };
  return (
    <div className="max-w-7xl mx-auto px-6 py-10 space-y-8">
      <h1 className="text-3xl font-bold">Admin Panel</h1>
      {stats && <div className="grid grid-cols-2 gap-4">
        <div className="glass p-6"><div className="text-slate-400 text-sm">Total Users</div><div className="text-3xl font-bold">{stats.totalUsers}</div></div>
        <div className="glass p-6"><div className="text-slate-400 text-sm">Total URLs</div><div className="text-3xl font-bold">{stats.totalUrls}</div></div>
      </div>}
      <div className="glass p-6">
        <h2 className="text-xl font-semibold mb-4">Users</h2>
        <table className="w-full"><thead><tr className="text-left text-slate-400"><th className="p-2">Email</th><th>Role</th><th>Enabled</th><th></th></tr></thead><tbody>
          {users.map(u=><tr key={u.id} className="border-t border-white/5"><td className="p-2">{u.email}</td><td>{u.role}</td><td>{String(u.enabled)}</td><td><button onClick={()=>disable(u.id)} className="btn-ghost">Disable</button></td></tr>)}
        </tbody></table>
      </div>
      <div className="glass p-6">
        <h2 className="text-xl font-semibold mb-4">URLs</h2>
        <table className="w-full"><thead><tr className="text-left text-slate-400"><th className="p-2">Code</th><th>Original</th><th>Clicks</th><th>Blocked</th><th></th></tr></thead><tbody>
          {urls.map(u=><tr key={u.id} className="border-t border-white/5"><td className="p-2">{u.shortCode}</td><td className="truncate max-w-xs">{u.originalUrl}</td><td>{u.clickCount}</td><td>{String(u.blocked)}</td><td><button onClick={()=>block(u.id)} className="btn-ghost text-red-400">Block</button></td></tr>)}
        </tbody></table>
      </div>
    </div>
  );
}
