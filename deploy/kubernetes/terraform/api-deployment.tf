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
            name  = "PROFILE"
            value = "prod"
          }

          env {
            name  = "DATABASE_URL"
            value = "jdbc:postgresql://10.0.1.77:5432/restaurant_db"
          }

          env {
            name  = "DATABASE_USERNAME"
            value = "taleh"
          }

          env {
            name  = "DATABASE_PASSWORD"
            value = "d239dj8j8dsf78sda9d87"
          }

          env {
            name  = "JWT_SIGN_KEY"
            value = "b432d7c7910b0c7bd5f53979224bd59518f8eb1c6ad4ec5a02facedcbd0661cc"
          }

          env {
            name  = "JWT_ACCESS_TOKEN_DURATION"
            value = "30"
          }

          env {
            name  = "JWT_REFRESH_TOKEN_DURATION"
            value = "2678400"
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
