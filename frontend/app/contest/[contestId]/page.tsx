'use client'

import { useState, useEffect } from 'react'
import { useParams, useRouter } from 'next/navigation'
import ContestInterface from '@/components/ContestInterface'
import LoadingSpinner from '@/components/LoadingSpinner'

interface Contest {
  id: number
  contestId: string
  name: string
  description: string
  startTime: string
  endTime: string
  problems: Problem[]
}

interface Problem {
  id: number
  title: string
  description: string
  sampleInput: string
  sampleOutput: string
  timeLimit: number
  memoryLimit: number
}

export default function ContestPage() {
  const params = useParams()
  const router = useRouter()
  const [contest, setContest] = useState<Contest | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  const contestId = params.contestId as string

  useEffect(() => {
    const fetchContest = async () => {
      try {
        const response = await fetch(`/api/contests/${contestId}`)
        
        if (!response.ok) {
          throw new Error('Contest not found')
        }
        
        const contestData = await response.json()
        setContest(contestData)
      } catch (err) {
        setError('Failed to load contest. Please check the contest ID.')
        console.error('Error fetching contest:', err)
      } finally {
        setLoading(false)
      }
    }

    // Check if user is logged in
    const username = localStorage.getItem('username')
    const storedContestId = localStorage.getItem('contestId')
    
    if (!username || storedContestId !== contestId) {
      router.push('/')
      return
    }

    fetchContest()
  }, [contestId, router])

  if (loading) {
    return <LoadingSpinner />
  }

  if (error) {
    return (
      <div className="max-w-md mx-auto">
        <div className="card p-8 text-center">
          <h2 className="text-xl font-semibold text-gray-900 mb-4">Error</h2>
          <p className="text-gray-600 mb-6">{error}</p>
          <button
            onClick={() => router.push('/')}
            className="btn-primary"
          >
            Back to Home
          </button>
        </div>
      </div>
    )
  }

  if (!contest) {
    return (
      <div className="max-w-md mx-auto">
        <div className="card p-8 text-center">
          <h2 className="text-xl font-semibold text-gray-900 mb-4">Contest Not Found</h2>
          <p className="text-gray-600 mb-6">The contest you're looking for doesn't exist.</p>
          <button
            onClick={() => router.push('/')}
            className="btn-primary"
          >
            Back to Home
          </button>
        </div>
      </div>
    )
  }

  return <ContestInterface contest={contest} />
}
