import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import api from '../api/client';
import toast from 'react-hot-toast';
import { Copy, Trash2, QrCode, BarChart3, Plus } from 'lucide-react';
import { motion } from 'framer-motion';

export default function Dashboard(){
  const [urls,setUrls]=useState([]); const [summary,setSummary]=useState(null);
  const [form,setForm]=useState({originalUrl:'',customAlias:'',expiresAt:'',password:'',oneTime:false});
  const [creating,setCreating]=useState(false);

  const load = async () => {
    const [u,s] = await Promise.all([api.get('/api/urls'), api.get('/api/analytics/summary')]);
    setUrls(u.data.content || []); setSummary(s.data);
  };
  useEffect(()=>{ load().catch(()=>toast.error('Failed to load')); },[]);

  const create = async e => {
    e.preventDefault(); setCreating(true);
    try {
      const payload = { ...form, expiresAt: form.expiresAt ? new Date(form.expiresAt).toISOString() : null };
      await api.post('/api/urls', payload);
      toast.success('Short link created');
      setForm({originalUrl:'',customAlias:'',expiresAt:'',password:'',oneTime:false});
      load();
    } catch(err){ toast.error(err.response?.data?.error || 'Failed'); }
    finally { setCreating(false); }
  };
  const remove = async id => { await api.delete(`/api/urls/${id}`); toast.success('Deleted'); load(); };
  const copy = t => { navigator.clipboard.writeText(t); toast.success('Copied'); };
  const qr = async id => {
    const { data } = await api.get(`/api/urls/${id}/qr`, { responseType:'blob' });
    window.open(URL.createObjectURL(data), '_blank');
  };

  return (
    <div className="max-w-7xl mx-auto px-6 py-10">
      <h1 className="text-3xl font-bold mb-8">Dashboard</h1>

      {summary && (
        <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mb-8">
          {[['Total URLs',summary.totalUrls],['Total Clicks',summary.totalClicks],['Active',summary.activeUrls],['Top link clicks',summary.topLinks?.[0]?.clicks ?? 0]].map(([l,v])=>(
            <motion.div key={l} initial={{opacity:0,y:10}} animate={{opacity:1,y:0}} className="glass p-6">
              <div className="text-slate-400 text-sm">{l}</div>
              <div className="text-3xl font-bold mt-2">{v}</div>
            </motion.div>
          ))}
        </div>
      )}

      <div className="glass p-6 mb-8">
        <h2 className="text-xl font-semibold mb-4 flex items-center gap-2"><Plus size={18}/> Create new short link</h2>
        <form onSubmit={create} className="grid md:grid-cols-2 gap-4">
          <input className="input md:col-span-2" placeholder="https://example.com/very/long/url" value={form.originalUrl} onChange={e=>setForm({...form,originalUrl:e.target.value})} required/>
          <input className="input" placeholder="Custom alias (optional)" value={form.customAlias} onChange={e=>setForm({...form,customAlias:e.target.value})}/>
          <input className="input" type="datetime-local" value={form.expiresAt} onChange={e=>setForm({...form,expiresAt:e.target.value})}/>
          <input className="input" type="password" placeholder="Password (optional)" value={form.password} onChange={e=>setForm({...form,password:e.target.value})}/>
          <label className="flex items-center gap-2 text-slate-300">
            <input type="checkbox" checked={form.oneTime} onChange={e=>setForm({...form,oneTime:e.target.checked})}/> One-time link
          </label>
          <button disabled={creating} className="btn-primary md:col-span-2">{creating?'Creating…':'Shorten URL'}</button>
        </form>
      </div>

      <div className="glass overflow-hidden">
        <table className="w-full text-left">
          <thead className="bg-white/5"><tr>
            <th className="p-4">Short</th><th className="p-4">Original</th><th className="p-4">Clicks</th><th className="p-4">Actions</th>
          </tr></thead>
          <tbody>
            {urls.length === 0 && <tr><td colSpan="4" className="p-8 text-center text-slate-400">No links yet</td></tr>}
            {urls.map(u => (
              <tr key={u.id} className="border-t border-white/5 hover:bg-white/5">
                <td className="p-4"><a href={u.shortUrl} target="_blank" className="text-indigo-400">{u.shortUrl}</a></td>
                <td className="p-4 truncate max-w-xs text-slate-300">{u.originalUrl}</td>
                <td className="p-4">{u.clickCount}</td>
                <td className="p-4 flex gap-2">
                  <button onClick={()=>copy(u.shortUrl)} className="btn-ghost p-2"><Copy size={16}/></button>
                  <button onClick={()=>qr(u.id)} className="btn-ghost p-2"><QrCode size={16}/></button>
                  <Link to={`/analytics/${u.id}`} className="btn-ghost p-2"><BarChart3 size={16}/></Link>
                  <button onClick={()=>remove(u.id)} className="btn-ghost p-2 text-red-400"><Trash2 size={16}/></button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
