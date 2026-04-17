variable "linked_project" {
  type        = string
  description = "Supabase project ref"
}

variable "organization_id" {
  type        = string
  description = "Supabase organization slug"
}

variable "project_name" {
  type        = string
  description = "Supabase project name"
}

variable "database_password" {
  type        = string
  description = "Database password for project reconciliation"
  sensitive   = true
}

variable "region" {
  type        = string
  description = "Supabase region"
}
