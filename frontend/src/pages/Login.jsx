import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import toast from 'react-hot-toast';

export default function Login(){
  const { login } = useAuth();
  const nav = useNavigate();
  const [email,setEmail]=useState(''); const [pw,setPw]=useState(''); const [loading,setLoading]=useState(false);
  const submit = async e => {
    e.preventDefault(); setLoading(true);
    try { await login(email, pw); toast.success('Welcome back'); nav('/dashboard'); }
    catch(err){ toast.error(err.response?.data?.error || 'Login failed'); }
    finally { setLoading(false); }
  };
  return (
    <div className="max-w-md mx-auto px-6 py-20">
      <div className="glass p-8">
        <h1 className="text-3xl font-bold mb-2">Welcome back</h1>
        <p className="text-slate-400 mb-6">Sign in to your LinkForge account</p>
        <form onSubmit={submit} className="space-y-4">
          <input className="input" type="email" placeholder="Email" value={email} onChange={e=>setEmail(e.target.value)} required/>
          <input className="input" type="password" placeholder="Password" value={pw} onChange={e=>setPw(e.target.value)} required/>
          <button disabled={loading} className="btn-primary w-full">{loading?'Signing in…':'Sign in'}</button>
        </form>
        <p className="text-slate-400 text-sm mt-6 text-center">No account? <Link to="/register" className="text-indigo-400">Create one</Link></p>
      </div>
    </div>
  );
}
