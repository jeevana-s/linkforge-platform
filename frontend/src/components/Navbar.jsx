import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { Link2 } from 'lucide-react';

export default function Navbar(){
  const { user, logout } = useAuth();
  const nav = useNavigate();
  return (
    <header className="sticky top-0 z-40 backdrop-blur-xl bg-slate-950/60 border-b border-white/5">
      <div className="max-w-7xl mx-auto px-6 h-16 flex items-center justify-between">
        <Link to="/" className="flex items-center gap-2 font-bold text-lg">
          <span className="w-8 h-8 rounded-lg bg-gradient-to-br from-indigo-500 to-pink-500 grid place-items-center"><Link2 size={16}/></span>
          LinkForge
        </Link>
        <nav className="flex items-center gap-2">
          {user ? (
            <>
              <Link to="/dashboard" className="btn-ghost">Dashboard</Link>
              {user.role === 'ADMIN' && <Link to="/admin" className="btn-ghost">Admin</Link>}
              <Link to="/profile" className="btn-ghost">{user.email}</Link>
              <button onClick={()=>{logout(); nav('/');}} className="btn-primary">Logout</button>
            </>
          ) : (
            <>
              <Link to="/login" className="btn-ghost">Login</Link>
              <Link to="/register" className="btn-primary">Get Started</Link>
            </>
          )}
        </nav>
      </div>
    </header>
  );
}
