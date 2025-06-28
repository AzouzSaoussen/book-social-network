FROM node:16 AS build-stage

WORKDIR /app

COPY package*.json ./

RUN npm install

COPY . .

RUN npx ng build --configuration=production

FROM nginx:alpine

COPY nginx.conf /etc/nginx/nginx.conf

COPY --from=build-stage /app/dist/book-network-ui /usr/share/nginx/html

EXPOSE 80