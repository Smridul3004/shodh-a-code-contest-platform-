'use client'

import { CheckCircle, XCircle, Clock, AlertCircle } from 'lucide-react'

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

export default function SubmissionStatus({ submission }: { submission: Submission }) {
  const getStatusIcon = () => {
    if (submission.status === 'PENDING') {
      return <Clock className="w-5 h-5 text-yellow-500" />
    }
    if (submission.status === 'RUNNING') {
      return <Clock className="w-5 h-5 text-blue-500 animate-spin" />
    }
    if (submission.result === 'ACCEPTED') {
      return <CheckCircle className="w-5 h-5 text-green-500" />
    }
    return <XCircle className="w-5 h-5 text-red-500" />
  }

  const getStatusText = () => {
    if (submission.status === 'PENDING') return 'Pending'
    if (submission.status === 'RUNNING') return 'Running'
    if (submission.result === 'ACCEPTED') return 'Accepted'
    if (submission.result === 'WRONG_ANSWER') return 'Wrong Answer'
    if (submission.result === 'TIME_LIMIT_EXCEEDED') return 'Time Limit Exceeded'
    if (submission.result === 'MEMORY_LIMIT_EXCEEDED') return 'Memory Limit Exceeded'
    if (submission.result === 'RUNTIME_ERROR') return 'Runtime Error'
    if (submission.result === 'COMPILATION_ERROR') return 'Compilation Error'
    return 'Unknown'
  }

  const getStatusClass = () => {
    if (submission.status === 'PENDING') return 'status-pending'
    if (submission.status === 'RUNNING') return 'status-running'
    if (submission.result === 'ACCEPTED') return 'status-accepted'
    if (submission.result === 'WRONG_ANSWER') return 'status-wrong-answer'
    if (submission.result === 'TIME_LIMIT_EXCEEDED') return 'status-time-limit'
    if (submission.result === 'RUNTIME_ERROR') return 'status-runtime-error'
    return 'status-pending'
  }

  const isCompleted = submission.status === 'COMPLETED'

  return (
    <div className="card p-6">
      <div className="flex items-center justify-between mb-4">
        <h2 className="text-lg font-semibold text-gray-900">Submission Status</h2>
        <div className="flex items-center space-x-2">
          {getStatusIcon()}
          <span className={getStatusClass()}>
            {getStatusText()}
          </span>
        </div>
      </div>

      <div className="space-y-3">
        <div className="flex justify-between text-sm">
          <span className="text-gray-500">Submission ID:</span>
          <span className="font-mono text-gray-900">{submission.submissionId}</span>
        </div>

        <div className="flex justify-between text-sm">
          <span className="text-gray-500">Problem:</span>
          <span className="text-gray-900">{submission.problemTitle}</span>
        </div>

        <div className="flex justify-between text-sm">
          <span className="text-gray-500">Submitted:</span>
          <span className="text-gray-900">
            {new Date(submission.submittedAt).toLocaleString()}
          </span>
        </div>

        {isCompleted && submission.executionTime && (
          <div className="flex justify-between text-sm">
            <span className="text-gray-500">Execution Time:</span>
            <span className="text-gray-900">{submission.executionTime}ms</span>
          </div>
        )}

        {isCompleted && submission.memoryUsed && (
          <div className="flex justify-between text-sm">
            <span className="text-gray-500">Memory Used:</span>
            <span className="text-gray-900">{submission.memoryUsed}MB</span>
          </div>
        )}

        {submission.errorMessage && (
          <div className="mt-4 p-3 bg-red-50 border border-red-200 rounded-lg">
            <div className="flex items-start space-x-2">
              <AlertCircle className="w-4 h-4 text-red-500 mt-0.5 flex-shrink-0" />
              <div>
                <div className="text-sm font-medium text-red-800">Error Details:</div>
                <div className="text-sm text-red-700 mt-1 whitespace-pre-wrap">
                  {submission.errorMessage}
                </div>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  )
}
