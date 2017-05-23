Hello ${complaint.user.firstName} ${complaint.user.lastName}!
Your complaint <#if complaint.productInstance??>regarding product ${complaint.productInstance.price.product.productName} </#if>by reason of ${complaint.complainReason.categoryName} was successfully consideredn.
Specialist: ${complaint.responsible.firstName} ${complaint.responsible.lastName}, e-mail: ${complaint.responsible.email}.

Complaint subject:

${complaint.complainTitle}
<#if complaint.content??>

Content:

${complaint.content}.
</#if>

Rasponse:

${complaint.response}