resource "kubernetes_deployment" "universal-grabber-backend-api2" {
  metadata {
    name = var.base_name

    labels = {
      app = var.base_name
    }
  }
  spec {
    selector {
      match_labels = {
        app = var.base_name
      }
    }

    template {
      metadata {
        name = var.base_name

        labels = {
          app = var.base_name
        }
      }
      spec {
        container {
          name  = var.base_name
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
