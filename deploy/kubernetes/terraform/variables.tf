variable "DOCKER_IMG_TAG" {}

variable "api_base_name" {
  type        = string
  description = "Base name"
  default     = "restaurant-app-api"
}

variable "ui_base_name" {
  type        = string
  description = "Base name"
  default     = "restaurant-app-ui"
}

locals {
  repository    = "hub.tisserv.net"
  api_service_image = "${local.repository}/${var.api_base_name}:${var.DOCKER_IMG_TAG}"
  ui_service_image = "${local.repository}/${var.ui_base_name}:${var.DOCKER_IMG_TAG}"
}
