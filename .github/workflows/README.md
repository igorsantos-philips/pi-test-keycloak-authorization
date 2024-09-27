# Guideline to use the DevOps workflows

## Initial setup
In order for any DevOps pipeline to run the repository needs to be configured with needed secrets, app and Webhooks. This process is automated and can be triggered here: [emr-devops-actions - setup repository](https://github.com/philips-internal/emr-devops-actions/actions/workflows/00_setup_repository.yml). <br>
If you don't have access please request DevOps team to trigger it. <br>
<br>
Uncomment the trigger lines whenever in a official repository.

## BlackDuck and Fortify
The Project/Application needs to be created with the BlackDuck/Fortify team before the first pipeline run. You can find their documentation as below: <br>
[BlackDuck](https://portal.internal.philips/docs/default/component/blackduck/#getting-started). <br>
[Fortify](https://portal.internal.philips/docs/default/component/fortify/#getting-started). <br>

Alternatively, you can request a project creation in the following link: [Global Ticketing](https://globalticketing.ta.philips.com/Home.aspx?view=PSSO) (you need to be in the global VPN)
### Important notes
1. DevOps and BlackDuck/Fortify are different teams. They host and manage the application and we provide a intermediate way to upload scans.
2. If opening a ticket, the AD_Group fields need to have the following groups: ggINGBTCPIC5-Fortify_AD_EMR and ggINGBTCPIC5-Fortify_SA_EMR. This will give access to the pipeline to upload the scan

## Sonar
The CI and PR pipelines are currently using Sonar Enterprise. You can run their automation and it will automatically configure your repository [Sonar Enterprise - Create Project](https://portal.internal.philips/create/templates/default/sonarqube-enterprise-create-project)