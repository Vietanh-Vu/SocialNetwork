/** @type {import('next').NextConfig} */
const nextConfig = {
    images: {
        remotePatterns: [
            {
                protocol: "https",
                hostname: "ghtk-socialnetwork.s3.ap-southeast-2.amazonaws.com",
                port: "",
                pathname: "/**",
            },
        ],
    },
    output: 'standalone'
};

export default nextConfig;
