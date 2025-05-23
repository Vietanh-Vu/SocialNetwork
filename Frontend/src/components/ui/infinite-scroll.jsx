"use client"
import * as React from 'react';

export default function InfiniteScroll({
                                           isLoading,
                                           hasMore,
                                           next,
                                           threshold = 0.8,
                                           rootMargin = '0px 0px 200px 0px',
                                           children,
                                       }) {
    const observerRef = React.useRef(null);
    const targetRef = React.useRef(null);

    React.useEffect(() => {
        const currentTarget = targetRef.current;
        if (!currentTarget) return;

        const safeThreshold = threshold < 0 || threshold > 1 ? 0.8 : threshold;

        const observer = new IntersectionObserver(
            (entries) => {
                if (entries[0].isIntersecting && hasMore && !isLoading) {
                    next();
                }
            },
            {threshold: safeThreshold, rootMargin}
        );

        observer.observe(currentTarget);
        observerRef.current = observer;

        return () => {
            if (observerRef.current) {
                observerRef.current.disconnect();
            }
        };
    }, [hasMore, isLoading, next, threshold, rootMargin]);

    return (
        <>
            {children}
            {hasMore && (
                <div ref={targetRef} className="h-4 w-full"/>
            )}
        </>
    );
}