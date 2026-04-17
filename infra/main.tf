import {
  to = supabase_project.this
  id = var.linked_project
}

resource "supabase_project" "this" {
  organization_id   = var.organization_id
  name              = var.project_name
  database_password = var.database_password
  region            = var.region

  lifecycle {
    ignore_changes = [database_password]
  }
}

resource "supabase_settings" "this" {
  project_ref = var.linked_project

  api = jsonencode({
    db_schema            = "public,storage,graphql_public"
    db_extra_search_path = "public,extensions"
    max_rows             = 1000
  })
}

output "project_ref" {
  value = var.linked_project
}

output "project_name" {
  value = supabase_project.this.name
}
