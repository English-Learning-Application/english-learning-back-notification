aws ecr get-login-password --region ap-southeast-2 | docker login --username AWS --password-stdin 761018889743.dkr.ecr.ap-southeast-2.amazonaws.com
docker build -t notification-microservice .
docker tag notification-microservice:latest 761018889743.dkr.ecr.ap-southeast-2.amazonaws.com/notification-microservice:latest
docker push 761018889743.dkr.ecr.ap-southeast-2.amazonaws.com/notification-microservice:latest
kubectl delete deployment notification-service-deployment
kubectl apply -f deployment.yaml