resource "kubernetes_deployment" "restaurant-app-api" {
  metadata {
    name = var.api_base_name

    labels = {
      app = var.api_base_name
    }
  }
  spec {
    selector {
      match_labels = {
        app = var.api_base_name
      }
    }

    template {
      metadata {
        name = var.api_base_name

        labels = {
          app = var.api_base_name
        }
      }
      spec {
        container {
          name  = var.api_base_name
          image = local.service_image

          env {
            name  = "SPRING_PROFILES_ACTIVE"
            value = "prod"
          }
        }

        image_pull_secrets {
          name = "tisserv-hub"
        }
      }
    }
    replicas = 1
  }
}
