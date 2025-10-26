'use client'

import { useState, useEffect } from 'react'
import ProblemView from './ProblemView'
import CodeEditor from './CodeEditor'
import Leaderboard from './Leaderboard'
import SubmissionStatus from './SubmissionStatus'

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

interface Submission {
  submissionId: string
  code: string
  status: string
  result: string | null
  errorMessage: string | null
  executionTime: number | null
  memoryUsed: number | null
  submittedAt: string
  username: string
  problemTitle: string
}

export default function ContestInterface({ contest }: { contest: Contest }) {
  const [selectedProblem, setSelectedProblem] = useState<Problem | null>(null)
  const [code, setCode] = useState('')
  const [submission, setSubmission] = useState<Submission | null>(null)
  const [isSubmitting, setIsSubmitting] = useState(false)

  // Load default code template when problem is selected
  useEffect(() => {
    if (selectedProblem && !code) {
      setCode(`import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Your code here
        
    }
}`)
    }
  }, [selectedProblem, code])

  const handleSubmit = async () => {
    if (!selectedProblem || !code.trim()) {
      alert('Please select a problem and write some code')
      return
    }

    setIsSubmitting(true)
    setSubmission(null)

    try {
      const username = localStorage.getItem('username')
      const response = await fetch('/api/submissions', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          username,
          problemId: selectedProblem.id,
          code,
        }),
      })

      if (!response.ok) {
        throw new Error('Failed to submit code')
      }

      const data = await response.json()
      
      // Start polling for submission status
      pollSubmissionStatus(data.submissionId)
      
    } catch (error) {
      console.error('Error submitting code:', error)
      alert('Failed to submit code. Please try again.')
    } finally {
      setIsSubmitting(false)
    }
  }

  const pollSubmissionStatus = async (submissionId: string) => {
    const pollInterval = setInterval(async () => {
      try {
        const response = await fetch(`/api/submissions/${submissionId}`)
        
        if (!response.ok) {
          throw new Error('Failed to fetch submission status')
        }

        const submissionData = await response.json()
        setSubmission(submissionData)

        // Stop polling if submission is completed
        if (submissionData.status === 'COMPLETED') {
          clearInterval(pollInterval)
        }
      } catch (error) {
        console.error('Error polling submission status:', error)
        clearInterval(pollInterval)
      }
    }, 2000) // Poll every 2 seconds
  }

  return (
    <div className="space-y-6">
      {/* Contest Header */}
      <div className="card p-6">
        <h1 className="text-2xl font-bold text-gray-900 mb-2">{contest.name}</h1>
        <p className="text-gray-600 mb-4">{contest.description}</p>
        <div className="flex items-center space-x-4 text-sm text-gray-500">
          <span>Contest ID: {contest.contestId}</span>
          <span>Problems: {contest.problems.length}</span>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Left Column - Problems and Code Editor */}
        <div className="lg:col-span-2 space-y-6">
          {/* Problem Selection */}
          <div className="card p-6">
            <h2 className="text-lg font-semibold text-gray-900 mb-4">Problems</h2>
            <div className="space-y-2">
              {contest.problems.map((problem) => (
                <button
                  key={problem.id}
                  onClick={() => setSelectedProblem(problem)}
                  className={`w-full text-left p-3 rounded-lg border transition-colors ${
                    selectedProblem?.id === problem.id
                      ? 'border-primary-500 bg-primary-50 text-primary-900'
                      : 'border-gray-200 hover:border-gray-300 hover:bg-gray-50'
                  }`}
                >
                  <div className="font-medium">{problem.title}</div>
                  <div className="text-sm text-gray-500">
                    Time Limit: {problem.timeLimit}s | Memory: {problem.memoryLimit}MB
                  </div>
                </button>
              ))}
            </div>
          </div>

          {/* Problem View */}
          {selectedProblem && (
            <ProblemView problem={selectedProblem} />
          )}

          {/* Code Editor */}
          {selectedProblem && (
            <CodeEditor
              code={code}
              onChange={setCode}
              onSubmit={handleSubmit}
              isSubmitting={isSubmitting}
            />
          )}

          {/* Submission Status */}
          {submission && (
            <SubmissionStatus submission={submission} />
          )}
        </div>

        {/* Right Column - Leaderboard */}
        <div className="lg:col-span-1">
          <Leaderboard contestId={contest.contestId} />
        </div>
      </div>
    </div>
  )
}
