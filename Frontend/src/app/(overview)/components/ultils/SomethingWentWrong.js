import {Button} from "@/components/ui/button"

function SomethingWentWrong() {
    return (
        <div
            className="flex min-h-[50vh] flex-col items-center justify-center bg-background px-4 py-12 sm:px-6 lg:px-8">
            <div className="mx-auto max-w-md text-center">
                <div className="mx-auto h-12 w-12 text-primary"/>
                <h2 className="mt-4 text-2xl font-bold tracking-tight text-foreground sm:text-3xl">
                    Oops, something went wrong!
                </h2>
                <p className="mt-4 text-muted-foreground">
                    We&apos;re sorry, but there was an issue fetching the data. Please try again later.
                </p>
            </div>
        </div>
    )
}

export default SomethingWentWrong;