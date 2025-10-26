'use client'

import { Prism as SyntaxHighlighter } from 'react-syntax-highlighter'
import { tomorrow } from 'react-syntax-highlighter/dist/esm/styles/prism'

interface Problem {
  id: number
  title: string
  description: string
  sampleInput: string
  sampleOutput: string
  timeLimit: number
  memoryLimit: number
}

export default function ProblemView({ problem }: { problem: Problem }) {
  return (
    <div className="card p-6">
      <h2 className="text-xl font-semibold text-gray-900 mb-4">{problem.title}</h2>
      
      <div className="prose max-w-none">
        <div className="whitespace-pre-wrap text-gray-700 mb-6">
          {problem.description}
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div>
          <h3 className="font-medium text-gray-900 mb-2">Sample Input</h3>
          <div className="bg-gray-100 p-3 rounded-lg">
            <SyntaxHighlighter
              language="text"
              style={tomorrow}
              customStyle={{
                margin: 0,
                background: 'transparent',
                fontSize: '14px',
              }}
            >
              {problem.sampleInput}
            </SyntaxHighlighter>
          </div>
        </div>

        <div>
          <h3 className="font-medium text-gray-900 mb-2">Sample Output</h3>
          <div className="bg-gray-100 p-3 rounded-lg">
            <SyntaxHighlighter
              language="text"
              style={tomorrow}
              customStyle={{
                margin: 0,
                background: 'transparent',
                fontSize: '14px',
              }}
            >
              {problem.sampleOutput}
            </SyntaxHighlighter>
          </div>
        </div>
      </div>

      <div className="mt-6 flex items-center space-x-4 text-sm text-gray-500">
        <span>Time Limit: {problem.timeLimit} seconds</span>
        <span>Memory Limit: {problem.memoryLimit} MB</span>
      </div>
    </div>
  )
}
