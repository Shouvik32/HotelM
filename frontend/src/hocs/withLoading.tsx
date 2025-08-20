import React from 'react'

interface WithLoadingProps {
  loading: boolean
}

export function withLoading<P extends object>(Component: React.ComponentType<P>) {
  return (props: P & WithLoadingProps) => {
    const { loading, ...rest } = props as WithLoadingProps & P
    if (loading) {
      return <div>Loading...</div>
    }
    return <Component {...(rest as P)} />
  }
}

export default withLoading