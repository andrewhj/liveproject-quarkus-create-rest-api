$name=kubectl -n kubernetes-dashboard get sa/admin-user -o jsonpath="{.secrets[0].name}"

kubectl -n kubernetes-dashboard get secret $name -o go-template="{{.data.token | base64decode}}"
