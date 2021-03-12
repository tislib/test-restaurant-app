terraform {
  backend "local" {
    path = "/var/tfstate/ts-restaurant-app-api.tfstate"
  }
}

provider "kubernetes" {
  config_path = "~/.kube/config"
  config_context = "minikube"
}
