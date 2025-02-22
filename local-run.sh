docker build -t notification-microservice .
minikube image load notification-microservice:latest
kubectl delete secret notification-service-secret
kubectl create secret generic notification-service-secret --from-env-file=local.env
kubectl delete deployment notification-service-deployment
kubectl apply -f local-deployment.yaml