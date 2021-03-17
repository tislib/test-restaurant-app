resource "kubernetes_deployment" "restaurant-app-ui" {
  metadata {
    name = var.ui_base_name

    labels = {
      app = var.ui_base_name
    }
  }
  spec {
    selector {
      match_labels = {
        app = var.ui_base_name
      }
    }

    template {
      metadata {
        name = var.ui_base_name

        labels = {
          app = var.ui_base_name
        }
      }
      spec {
        container {
          name  = var.ui_base_name
          image = local.ui_service_image
        }

        image_pull_secrets {
          name = "tisserv-hub"
        }
      }
    }
    replicas = 2
  }
}
