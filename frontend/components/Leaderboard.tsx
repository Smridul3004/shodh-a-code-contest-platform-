'use client'

import { useState, useEffect } from 'react'
import { Trophy, Medal, Award } from 'lucide-react'

interface LeaderboardEntry {
  username: string
  problemsSolved: number
  totalSubmissions: number
  lastSubmissionTime: string | null
  rank: number
}

interface LeaderboardProps {
  contestId: string
}

export default function Leaderboard({ contestId }: LeaderboardProps) {
  const [leaderboard, setLeaderboard] = useState<LeaderboardEntry[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  const fetchLeaderboard = async () => {
    try {
      const response = await fetch(`/api/contests/${contestId}/leaderboard`)
      
      if (!response.ok) {
        throw new Error('Failed to fetch leaderboard')
      }
      
      const data = await response.json()
      setLeaderboard(data)
      setError('')
    } catch (err) {
      setError('Failed to load leaderboard')
      console.error('Error fetching leaderboard:', err)
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    fetchLeaderboard()
    
    // Poll leaderboard every 30 seconds
    const interval = setInterval(fetchLeaderboard, 30000)
    
    return () => clearInterval(interval)
  }, [contestId])

  const getRankIcon = (rank: number) => {
    if (rank === 1) return <Trophy className="w-5 h-5 text-yellow-500" />
    if (rank === 2) return <Medal className="w-5 h-5 text-gray-400" />
    if (rank === 3) return <Award className="w-5 h-5 text-amber-600" />
    return <span className="w-5 h-5 flex items-center justify-center text-sm font-medium text-gray-500">#{rank}</span>
  }

  if (loading) {
    return (
      <div className="card p-6">
        <h2 className="text-lg font-semibold text-gray-900 mb-4">Leaderboard</h2>
        <div className="space-y-3">
          {[1, 2, 3].map((i) => (
            <div key={i} className="animate-pulse">
              <div className="h-12 bg-gray-200 rounded-lg"></div>
            </div>
          ))}
        </div>
      </div>
    )
  }

  if (error) {
    return (
      <div className="card p-6">
        <h2 className="text-lg font-semibold text-gray-900 mb-4">Leaderboard</h2>
        <div className="text-center text-gray-500">
          <p>{error}</p>
          <button
            onClick={fetchLeaderboard}
            className="mt-2 text-primary-600 hover:text-primary-700 text-sm"
          >
            Try Again
          </button>
        </div>
      </div>
    )
  }

  return (
    <div className="card p-6">
      <div className="flex items-center justify-between mb-4">
        <h2 className="text-lg font-semibold text-gray-900">Leaderboard</h2>
        <div className="text-xs text-gray-500">
          Updates every 30s
        </div>
      </div>

      {leaderboard.length === 0 ? (
        <div className="text-center text-gray-500 py-8">
          <p>No submissions yet</p>
        </div>
      ) : (
        <div className="space-y-3">
          {leaderboard.map((entry, index) => (
            <div
              key={entry.username}
              className={`flex items-center space-x-3 p-3 rounded-lg ${
                index < 3 ? 'bg-gradient-to-r from-yellow-50 to-yellow-100 border border-yellow-200' : 'bg-gray-50'
              }`}
            >
              <div className="flex-shrink-0">
                {getRankIcon(entry.rank)}
              </div>
              
              <div className="flex-1 min-w-0">
                <div className="font-medium text-gray-900 truncate">
                  {entry.username}
                </div>
                <div className="text-sm text-gray-500">
                  {entry.problemsSolved} problem{entry.problemsSolved !== 1 ? 's' : ''} solved
                </div>
              </div>
              
              <div className="text-right text-sm text-gray-500">
                <div>{entry.totalSubmissions} submission{entry.totalSubmissions !== 1 ? 's' : ''}</div>
                {entry.lastSubmissionTime && (
                  <div className="text-xs">
                    {new Date(entry.lastSubmissionTime).toLocaleTimeString()}
                  </div>
                )}
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  )
}
