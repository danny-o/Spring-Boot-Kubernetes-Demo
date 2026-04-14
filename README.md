# 🚀 Spring Boot on Kubernetes — Demo Project

A Spring Boot application that demonstrates how to configure and deploy an application using [Kubernetes](https://kubernetes.io/). It includes practical examples of key Kubernetes resources such as Deployments, Services, Secrets, and ConfigMaps and illustrates how multi-component applications can be wired together within a Kubernetes environment.

While the guide below entails running Kubernetes locally using Minikube, the Kubernetes resources and **kubectl** commands shown are applicable to applications running in production environments.

---

## 📋 Table of Contents

- [Prerequisites](#-prerequisites)
- [Project Structure](#-project-structure)
- [Setup and Running the App](#%EF%B8%8F-setup-and-running-the-app)
- [Accessing the App](#-accessing-the-app)
- [Useful kubectl Commands](#%EF%B8%8F-useful-kubectl-commands)
- [Spring Boot Tech Stack](#-spring-boot-tech-stack)
- [Teardown](#-teardown)

---

## ✅ Prerequisites

Make sure the following tools are installed before proceeding:

- [Docker](https://docs.docker.com/get-docker/)
- [Minikube](https://minikube.sigs.k8s.io/docs/start/)

Verify your installations:

```bash

docker --version
minikube version
```

---

## 📁 Project Structure

```
.
├── src/                        # Spring Boot application source
├── infra/                      # Kubernetes configuration files
│   ├── postgres-config.yaml    # Postgres ConfigMap (env vars)
│   ├── postgres-secret.yaml    # Postgres credentials (base64-encoded)
│   ├── web-app-config.yaml     # App ConfigMap (env vars)
│   ├── web-app-secret.yaml     # App secrets (base64-encoded)
│   ├── postgres.yaml           # Postgres Deployment + Service
│   └── web-app.yaml            # Web app Deployment + Service (NodePort)
├── Dockerfile
└── ...
```

---

## ⚙️ Setup and Running the App

### 1. Start Minikube

Start Minikube using Docker as the driver. Since this project uses Docker for building the app image, specifying `--driver=docker` ensures Minikube runs as a Docker container and keeps everything consistent.

```bash
minikube start --driver=docker
```

### 2. Build the web app Docker image directly into Minikube

```bash
minikube image build -t webapp:v1 .
```

This builds from the `Dockerfile` in the current directory and tags the image as `webapp:v1`, which matches the image name configured in `infra/web-app.yaml`.

### 3. Apply the Kubernetes manifests

Apply all the configuration files in the `infra/` folder. Apply secrets and ConfigMaps before the workloads that depend on them:
> **Note:** Minikube ships with a built-in kubectl that is automatically configured to talk to your local cluster when you run `minikube start`

```bash
kubectl apply -f infra/postgres-secret.yaml
kubectl apply -f infra/postgres-config.yaml
kubectl apply -f infra/web-app-secret.yaml
kubectl apply -f infra/web-app-config.yaml
kubectl apply -f infra/postgres.yaml
kubectl apply -f infra/web-app.yaml
```

Or apply the entire folder at once — Kubernetes handles resource ordering by type:

```bash
kubectl apply -f infra/
```

### 4. Verify the pods are running

```bash
kubectl get pods
```

Wait until both the `postgres` and `webapp` pods show a `Running` status with all containers ready (e.g. `1/1`). This may take a minute on the first run while images are pulled and the database initialises.

---

## 🌐 Accessing the App

The web app is exposed via a **NodePort** service named `webapp-service`. Because Minikube runs inside a container, the NodePort is not directly reachable at `localhost` — use Minikube to get the correct URL.

**Get the URL (works on all platforms):**

```bash
minikube service --url webapp-service
```

This prints a URL (e.g. `http://192.168.49.2:31234`) you can open in your browser.

---

## 🛠️ Useful kubectl Commands

These commands help you explore, inspect, and debug the running application. These commands are also applicable to production applications as kubectl is the command-line interface for Kubernetes.

### 📋 List resources

```bash
# List all pods
kubectl get pods

# List all services
kubectl get services

# List all deployments
kubectl get deployments

# List everything at once
kubectl get all
```

### 🔍 Describe a resource

`describe` gives you detailed information about a resource — events, configuration, injected environment variables, and more. It's the first place to look when something isn't starting correctly.

```bash
# Describe a pod (replace <pod-name> with the actual name from kubectl get pods)
kubectl describe pod <pod-name>

# Describe the webapp deployment
kubectl describe deployment webapp

# Describe the webapp service
kubectl describe service webapp-service
```

### 📄 View logs

```bash
# View logs from a pod
kubectl logs <pod-name>

# Follow (tail) logs in real time
kubectl logs -f <pod-name>

# View logs from a previously crashed container (useful after a CrashLoopBackOff)
kubectl logs <pod-name> --previous
```

### 💻 Execute commands inside a pod

Useful for inspecting the container environment or connecting to the database directly:

```bash
# Open a shell inside the webapp pod
kubectl exec -it <pod-name> -- /bin/sh

# Connect to Postgres inside the postgres pod
kubectl exec -it <postgres-pod-name> -- psql -U <db-user> -d <db-name>
```

### 🗑️ Delete resources

```bash
# Delete a specific pod
# Note: if it belongs to a Deployment, Kubernetes will automatically restart it
kubectl delete pod <pod-name>

# Delete the webapp deployment
kubectl delete deployment webapp

# Remove everything applied from the infra folder
kubectl delete -f infra/
```

### 🔐 Inspect ConfigMaps and Secrets

```bash
# List ConfigMaps
kubectl get configmaps

# View the contents of a ConfigMap
kubectl describe configmap webapp-config

# List Secrets (values are base64-encoded and not shown by default)
kubectl get secrets
```

---

## 🧰 Spring Boot Tech Stack

| Layer | Technology |
|---|---|
| **Database** | PostgreSQL |
| **Database Migrations** | Flyway |
| **ORM / Data Access** | Spring Data JPA (Hibernate) |
| **Templating** | Thymeleaf |
| **Authentication** | Username/password auth with sessions managed via JWTs in HTTP-only cookies |
| **Containerisation** | Docker |
| **Orchestration** | Kubernetes (Minikube) |

---

## 🧹 Teardown

To stop the app and free up resources:

```bash
# Remove all deployed Kubernetes resources
kubectl delete -f infra/

# Stop the Minikube cluster (preserves state for next time)
minikube stop

# Optionally delete the cluster entirely to start fresh
minikube delete
```
