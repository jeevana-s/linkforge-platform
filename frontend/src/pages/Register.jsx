import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import toast from 'react-hot-toast';

export default function Register(){
  const { register } = useAuth();
  const nav = useNavigate();
  const [form,setForm]=useState({email:'',password:'',name:''}); const [loading,setLoading]=useState(false);
  const submit = async e => {
    e.preventDefault(); setLoading(true);
    try { await register(form.email, form.password, form.name); toast.success('Account created'); nav('/dashboard'); }
    catch(err){ toast.error(err.response?.data?.error || 'Registration failed'); }
    finally { setLoading(false); }
  };
  return (
    <div className="max-w-md mx-auto px-6 py-20">
      <div className="glass p-8">
        <h1 className="text-3xl font-bold mb-2">Create your account</h1>
        <p className="text-slate-400 mb-6">Start shortening in seconds</p>
        <form onSubmit={submit} className="space-y-4">
          <input className="input" placeholder="Name" value={form.name} onChange={e=>setForm({...form,name:e.target.value})}/>
          <input className="input" type="email" placeholder="Email" value={form.email} onChange={e=>setForm({...form,email:e.target.value})} required/>
          <input className="input" type="password" placeholder="Password (min 6)" value={form.password} onChange={e=>setForm({...form,password:e.target.value})} required/>
          <button disabled={loading} className="btn-primary w-full">{loading?'Creating…':'Create account'}</button>
        </form>
        <p className="text-slate-400 text-sm mt-6 text-center">Have an account? <Link to="/login" className="text-indigo-400">Sign in</Link></p>
      </div>
    </div>
  );
}
