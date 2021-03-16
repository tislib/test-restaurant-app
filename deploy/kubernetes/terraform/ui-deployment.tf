resource "kubernetes_deployment" "restaurant-app-ui" {
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
        }

        image_pull_secrets {
          name = "tisserv-hub"
        }
      }
    }
    replicas = 1
  }
}
