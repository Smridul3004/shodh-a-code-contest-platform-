'use client'

import { useState } from 'react'
import { useRouter } from 'next/navigation'

export default function Home() {
  const [contestId, setContestId] = useState('')
  const [username, setUsername] = useState('')
  const [isLoading, setIsLoading] = useState(false)
  const [error, setError] = useState('')
  const router = useRouter()

  const handleJoinContest = async (e: React.FormEvent) => {
    e.preventDefault()
    setIsLoading(true)
    setError('')

    try {
      // Validate inputs
      if (!contestId.trim() || !username.trim()) {
        setError('Please enter both contest ID and username')
        return
      }

      // Check if contest exists
      const response = await fetch(`/api/contests/${contestId}`)
      
      if (!response.ok) {
        setError('Contest not found. Please check the contest ID.')
        return
      }

      // Store user info in localStorage and navigate to contest
      localStorage.setItem('username', username)
      localStorage.setItem('contestId', contestId)
      router.push(`/contest/${contestId}`)
      
    } catch (err) {
      setError('Failed to join contest. Please try again.')
      console.error('Error joining contest:', err)
    } finally {
      setIsLoading(false)
    }
  }

  return (
    <div className="max-w-md mx-auto">
      <div className="card p-8">
        <div className="text-center mb-8">
          <h1 className="text-3xl font-bold text-gray-900 mb-2">
            Join Contest
          </h1>
          <p className="text-gray-600">
            Enter your contest ID and username to start coding
          </p>
        </div>

        <form onSubmit={handleJoinContest} className="space-y-6">
          <div>
            <label htmlFor="contestId" className="block text-sm font-medium text-gray-700 mb-2">
              Contest ID
            </label>
            <input
              type="text"
              id="contestId"
              value={contestId}
              onChange={(e) => setContestId(e.target.value)}
              placeholder="Enter contest ID (e.g., CONTEST001)"
              className="input-field"
              disabled={isLoading}
            />
          </div>

          <div>
            <label htmlFor="username" className="block text-sm font-medium text-gray-700 mb-2">
              Username
            </label>
            <input
              type="text"
              id="username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              placeholder="Enter your username"
              className="input-field"
              disabled={isLoading}
            />
          </div>

          {error && (
            <div className="bg-error-50 border border-error-200 text-error-700 px-4 py-3 rounded-lg">
              {error}
            </div>
          )}

          <button
            type="submit"
            disabled={isLoading}
            className="w-full btn-primary disabled:opacity-50 disabled:cursor-not-allowed"
          >
            {isLoading ? 'Joining...' : 'Join Contest'}
          </button>
        </form>

        <div className="mt-8 p-4 bg-blue-50 rounded-lg">
          <h3 className="font-medium text-blue-900 mb-2">Sample Contest</h3>
          <p className="text-sm text-blue-700">
            Try using contest ID: <code className="bg-blue-100 px-1 rounded">CONTEST001</code>
          </p>
        </div>
      </div>
    </div>
  )
}
