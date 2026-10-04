# Stage 1: Build React/Vite application
FROM node:20-alpine AS builder

WORKDIR /app

# Copy package files and application source code
COPY package*.json ./
COPY . .

# Install dependencies and build dist if missing
RUN if [ ! -d dist ]; then npm install && npm run build; fi

# Stage 2: Production web server using Nginx
FROM nginx:alpine

# Copy custom Nginx configuration
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Copy compiled static assets from builder stage
COPY --from=builder /app/dist /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
