resource "kubernetes_service" "restaurant-app-api" {
  metadata {
    name = var.base_name

    labels = {
      app = var.base_name
    }
  }
  spec {
    selector = {
      app = var.base_name
    }

    type = "NodePort"

    port {
      name        = "http"
      port        = 80
      node_port = 30963
      target_port = 8080
    }
  }
}
