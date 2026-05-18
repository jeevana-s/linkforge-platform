import { motion } from 'framer-motion';
import { Link } from 'react-router-dom';
import {
  BarChart3,
  Zap,
  Shield,
  Globe,
  QrCode,
  Lock
} from 'lucide-react';

const features = [
  {
    icon: Zap,
    title: 'Lightning Redirects',
    desc: 'Sub-millisecond redirects powered by Redis edge cache.'
  },
  {
    icon: BarChart3,
    title: 'Deep Analytics',
    desc: 'Browser, device, country, time-series — all in real time.'
  },
  {
    icon: Shield,
    title: 'Enterprise Security',
    desc: 'JWT auth, rate limiting, malicious URL detection.'
  },
  {
    icon: Globe,
    title: 'Scales Globally',
    desc: 'Stateless Spring Boot + Kafka async pipeline.'
  },
  {
    icon: QrCode,
    title: 'QR Codes',
    desc: 'Auto-generated QR for every link.'
  },
  {
    icon: Lock,
    title: 'Password & One-Time',
    desc: 'Protect, expire, or burn links after use.'
  },
];

export default function Landing() {
  return (
    <div className="relative overflow-hidden">

      <div className="absolute inset-0 bg-hero-gradient opacity-30 blur-3xl" />

      {/* HERO SECTION */}
      <section className="relative max-w-7xl mx-auto px-6 py-28 text-center">

        <motion.h1
          initial={{ opacity: 0, y: 20 }}
          animate={{ opacity: 1, y: 0 }}
          className="text-5xl md:text-7xl font-extrabold tracking-tight"
        >
          Shorten, Track, and{' '}
          <span className="bg-clip-text text-transparent bg-gradient-to-r from-indigo-400 to-pink-400">
            Scale
          </span>{' '}
          Your Links Instantly.
        </motion.h1>

        <motion.p
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          transition={{ delay: 0.2 }}
          className="mt-6 text-lg text-slate-400 max-w-2xl mx-auto"
        >
          The distributed URL platform built for modern teams.
          Premium analytics, blazing redirects, enterprise security.
        </motion.p>

        <div className="mt-10 flex justify-center gap-4">
          <Link to="/register" className="btn-primary text-lg">
            Start Free
          </Link>

          <a href="#features" className="btn-ghost text-lg">
            See Features
          </a>
        </div>

        <div className="mt-16 grid grid-cols-2 md:grid-cols-4 gap-4 max-w-3xl mx-auto">
          {[
            ['10M+', 'Links'],
            ['99.99%', 'Uptime'],
            ['<5ms', 'Redirect'],
            ['180+', 'Countries'],
          ].map(([v, l]) => (
            <div key={l} className="glass p-6">
              <div className="text-3xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-indigo-400 to-pink-400">
                {v}
              </div>

              <div className="text-slate-400 text-sm mt-1">
                {l}
              </div>
            </div>
          ))}
        </div>
      </section>

      {/* FEATURES */}
      <section
        id="features"
        className="relative max-w-7xl mx-auto px-6 py-20"
      >
        <h2 className="text-4xl font-bold text-center mb-12">
          Built for scale
        </h2>

        <div className="grid md:grid-cols-3 gap-6">
          {features.map((f, i) => (
            <motion.div
              key={f.title}
              initial={{ opacity: 0, y: 20 }}
              whileInView={{ opacity: 1, y: 0 }}
              viewport={{ once: true }}
              transition={{ delay: i * 0.05 }}
              className="glass p-6"
            >
              <f.icon className="text-indigo-400 mb-4" />

              <h3 className="text-xl font-semibold">
                {f.title}
              </h3>

              <p className="text-slate-400 mt-2">
                {f.desc}
              </p>
            </motion.div>
          ))}
        </div>
      </section>

      {/* PRICING */}
      <section className="relative max-w-5xl mx-auto px-6 py-20">

        <h2 className="text-4xl font-bold text-center mb-12">
          Simple pricing
        </h2>

        <div className="grid md:grid-cols-3 gap-6">

          {/* FREE */}
          <div className="glass p-8 transition-all duration-500 hover:scale-105 hover:ring-2 hover:ring-indigo-500 hover:shadow-2xl hover:shadow-indigo-500/30">

            <h3 className="text-xl font-semibold">
              Free
            </h3>

            <div className="text-4xl font-bold mt-4">
              $0
              <span className="text-base text-slate-400">
                /mo
              </span>
            </div>

            <ul className="mt-6 space-y-2 text-slate-300">
              <li>• 1,000 links</li>
              <li>• Basic analytics</li>
              <li>• Community support</li>
            </ul>

            <button
              onClick={() => window.location.href = "/register"}
              className="btn-primary w-full text-center inline-block mt-8"
            >
              Get Started
            </button>
          </div>

          {/* PRO */}
          <div className="glass p-8 transition-all duration-500 hover:scale-105 hover:ring-2 hover:ring-indigo-500 hover:shadow-2xl hover:shadow-indigo-500/30">

            <h3 className="text-xl font-semibold">
              Pro
            </h3>

            <div className="text-4xl font-bold mt-4">
              $19
              <span className="text-base text-slate-400">
                /mo
              </span>
            </div>

            <ul className="mt-6 space-y-2 text-slate-300">
              <li>• Unlimited links</li>
              <li>• Advanced analytics</li>
              <li>• Custom aliases</li>
              <li>• Priority support</li>
            </ul>

            <button
              onClick={() => alert("Pro plan coming soon!")}
              className="btn-primary w-full text-center inline-block mt-8"
            >
              Upgrade Plan
            </button>
          </div>

          {/* ENTERPRISE */}
        <div className="glass p-8 transition-all duration-500 hover:scale-105 hover:ring-2 hover:ring-indigo-500 hover:shadow-2xl hover:shadow-indigo-500/30">

            <h3 className="text-xl font-semibold">
              Enterprise
            </h3>

            <div className="text-4xl font-bold mt-4">
              Custom
              <span className="text-base text-slate-400">
                /mo
              </span>
            </div>

            <ul className="mt-6 space-y-2 text-slate-300">
              <li>• SSO & SAML</li>
              <li>• SLA</li>
              <li>• Dedicated infra</li>
              <li>• 24/7 support</li>
            </ul>

            <button
              onClick={() => window.location.href = "mailto:support@linkforge.com"}
              className="btn-primary w-full text-center inline-block mt-8"
            >
              Contact Sales
            </button>
          </div>

        </div>
      </section>

      {/* FOOTER */}
      <footer className="relative border-t border-white/5 mt-20 py-10 text-center text-slate-500">
        © {new Date().getFullYear()} LinkForge.
        Built with Spring Boot, Redis, Kafka, React.
      </footer>

    </div>
  );
}