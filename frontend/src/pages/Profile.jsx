import { useAuth } from '../context/AuthContext';
export default function Profile(){
  const { user } = useAuth();
  return (
    <div className="max-w-2xl mx-auto px-6 py-10">
      <div className="glass p-8">
        <h1 className="text-3xl font-bold mb-6">Profile</h1>
        <div className="space-y-3 text-slate-300">
          <div><span className="text-slate-400">Email:</span> {user?.email}</div>
          <div><span className="text-slate-400">Role:</span> {user?.role}</div>
        </div>
      </div>
    </div>
  );
}
