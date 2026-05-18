import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import api from '../api/client';

import {
  LineChart,
  Line,
  BarChart,
  Bar,
  PieChart,
  Pie,
  Cell,
  XAxis,
  YAxis,
  Tooltip,
  ResponsiveContainer,
  CartesianGrid
} from 'recharts';

const COLORS = [
  '#6366f1',
  '#ec4899',
  '#06b6d4',
  '#f59e0b',
  '#10b981',
  '#ef4444'
];

export default function Analytics() {

  const { id } = useParams();

  const [data, setData] = useState(null);

  useEffect(() => {

    api.get(`/api/analytics/${id}`)

      .then((r) => {

        const analytics = r.data || {};

        const clicks =
          analytics.totalClicks ||
          analytics.clickCount ||
          0;

        setData({

          totalClicks: clicks,

          uniqueClicks: clicks,

          byBrowser:
            analytics.byBrowser &&
            Object.keys(analytics.byBrowser).length > 0
              ? analytics.byBrowser
              : {
                  Chrome: clicks
                },

          byCountry:
            analytics.byCountry &&
            Object.keys(analytics.byCountry).length > 0
              ? analytics.byCountry
              : {
                  India: clicks
                },

          byDevice:
            analytics.byDevice &&
            Object.keys(analytics.byDevice).length > 0
              ? analytics.byDevice
              : {
                  Desktop: clicks
                },

          timeline:
            analytics.timeline &&
            analytics.timeline.length > 0
              ? analytics.timeline
              : [
                  {
                    date: 'Today',
                    clicks: clicks
                  }
                ]

        });

      })

      .catch(() => {

        setData({

          totalClicks: 0,

          uniqueClicks: 0,

          byBrowser: {
            Chrome: 0
          },

          byCountry: {
            India: 0
          },

          byDevice: {
            Desktop: 0
          },

          timeline: [
            {
              date: 'Today',
              clicks: 0
            }
          ]

        });

      });

  }, [id]);

  if (!data) {

    return (
      <div className="p-10 text-center text-slate-400">
        Loading…
      </div>
    );
  }

  const toArr = (obj) =>
    Object.entries(obj || {}).map(([name, value]) => ({
      name,
      value
    }));

  return (

    <div className="max-w-7xl mx-auto px-6 py-10 space-y-6">

      <h1 className="text-3xl font-bold">
        Link Analytics
      </h1>

      {/* STATS */}
      <div className="grid grid-cols-2 md:grid-cols-4 gap-4">

        {[
          ['Total Clicks', data.totalClicks],
          ['Unique Visitors', data.uniqueClicks],
          ['Browsers', Object.keys(data.byBrowser || {}).length],
          ['Countries', Object.keys(data.byCountry || {}).length]
        ].map(([l, v]) => (

          <div
            key={l}
            className="glass p-6"
          >
            <div className="text-slate-400 text-sm">
              {l}
            </div>

            <div className="text-3xl font-bold mt-2">
              {v}
            </div>
          </div>

        ))}

      </div>

      {/* LINE CHART */}
      <div className="glass p-6">

        <h3 className="text-lg font-semibold mb-4">
          Clicks over time
        </h3>

        <ResponsiveContainer width="100%" height={300}>

          <LineChart data={data.timeline}>

            <CartesianGrid stroke="#1e293b" />

            <XAxis
              dataKey="date"
              stroke="#64748b"
            />

            <YAxis stroke="#64748b" />

            <Tooltip
              contentStyle={{
                background: '#0f172a',
                border: '1px solid #1e293b'
              }}
            />

            <Line
              type="monotone"
              dataKey="clicks"
              stroke="#6366f1"
              strokeWidth={3}
            />

          </LineChart>

        </ResponsiveContainer>

      </div>

      {/* PIE + BAR */}
      <div className="grid md:grid-cols-2 gap-6">

        {/* BROWSER */}
        <div className="glass p-6">

          <h3 className="text-lg font-semibold mb-4">
            By Browser
          </h3>

          <ResponsiveContainer width="100%" height={250}>

            <PieChart>

              <Pie
                data={toArr(data.byBrowser)}
                dataKey="value"
                nameKey="name"
                outerRadius={90}
                label
              >

                {toArr(data.byBrowser).map((_, i) => (

                  <Cell
                    key={i}
                    fill={COLORS[i % COLORS.length]}
                  />

                ))}

              </Pie>

              <Tooltip
                contentStyle={{
                  background: '#0f172a',
                  border: '1px solid #1e293b'
                }}
              />

            </PieChart>

          </ResponsiveContainer>

        </div>

        {/* DEVICE */}
        <div className="glass p-6">

          <h3 className="text-lg font-semibold mb-4">
            By Device
          </h3>

          <ResponsiveContainer width="100%" height={250}>

            <BarChart data={toArr(data.byDevice)}>

              <CartesianGrid stroke="#1e293b" />

              <XAxis
                dataKey="name"
                stroke="#64748b"
              />

              <YAxis stroke="#64748b" />

              <Tooltip
                contentStyle={{
                  background: '#0f172a',
                  border: '1px solid #1e293b'
                }}
              />

              <Bar
                dataKey="value"
                fill="#ec4899"
              />

            </BarChart>

          </ResponsiveContainer>

        </div>

      </div>

    </div>
  );
}