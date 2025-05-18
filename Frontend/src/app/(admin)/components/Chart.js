"use client"

import * as React from "react"

export const Chart = ({ data, dataKey, categories, colors, valueFormatter, startEndOnly, children }) => {
    return React.Children.only(children)
}

export const ChartContainer = ({ className, children }) => {
    return <div className={className}>{children}</div>
}

export const ChartTooltip = ({ children }) => {
    return <>{children}</>
}

export const ChartTooltipContent = () => {
    return <></>
}

export const ChartGrid = ({ horizontal, vertical }) => {
    return null
}

export const ChartLine = () => {
    return null
}

export const ChartXAxis = ({ tickCount, tickFormat }) => {
    return null
}

export const ChartYAxis = () => {
    return null
}

export const ChartArea = () => {
    return null
}
