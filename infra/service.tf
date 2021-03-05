resource "kubernetes_service" "universal-grabber-backend-api2" {
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
      node_port = 30003
      target_port = 8080
    }
  }
}
