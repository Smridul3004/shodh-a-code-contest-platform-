'use client'

import { useState } from 'react'
import Editor from '@monaco-editor/react'

interface CodeEditorProps {
  code: string
  onChange: (code: string) => void
  onSubmit: () => void
  isSubmitting: boolean
}

export default function CodeEditor({ code, onChange, onSubmit, isSubmitting }: CodeEditorProps) {
  const [isEditorReady, setIsEditorReady] = useState(false)

  const handleEditorDidMount = () => {
    setIsEditorReady(true)
  }

  const handleEditorChange = (value: string | undefined) => {
    onChange(value || '')
  }

  return (
    <div className="card p-6">
      <div className="flex items-center justify-between mb-4">
        <h2 className="text-lg font-semibold text-gray-900">Code Editor</h2>
        <div className="text-sm text-gray-500">
          Language: Java
        </div>
      </div>

      <div className="border border-gray-300 rounded-lg overflow-hidden">
        <Editor
          height="400px"
          defaultLanguage="java"
          value={code}
          onChange={handleEditorChange}
          onMount={handleEditorDidMount}
          options={{
            minimap: { enabled: false },
            fontSize: 14,
            lineNumbers: 'on',
            roundedSelection: false,
            scrollBeyondLastLine: false,
            automaticLayout: true,
            tabSize: 4,
            insertSpaces: true,
            wordWrap: 'on',
          }}
          theme="vs-light"
        />
      </div>

      <div className="mt-4 flex justify-end">
        <button
          onClick={onSubmit}
          disabled={isSubmitting || !isEditorReady || !code.trim()}
          className="btn-primary disabled:opacity-50 disabled:cursor-not-allowed"
        >
          {isSubmitting ? 'Submitting...' : 'Submit Code'}
        </button>
      </div>
    </div>
  )
}
