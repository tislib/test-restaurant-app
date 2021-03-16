resource "kubernetes_service" "restaurant-app-ui" {
  metadata {
    name = var.api_base_name

    labels = {
      app = var.api_base_name
    }
  }
  spec {
    selector = {
      app = var.api_base_name
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
