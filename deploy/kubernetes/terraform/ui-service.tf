resource "kubernetes_service" "restaurant-app-ui" {
  metadata {
    name = var.ui_base_name

    labels = {
      app = var.ui_base_name
    }
  }
  spec {
    selector = {
      app = var.ui_base_name
    }

    type = "NodePort"

    port {
      name        = "http"
      port        = 80
      node_port = 30693
      target_port = 8080
    }
  }
}
