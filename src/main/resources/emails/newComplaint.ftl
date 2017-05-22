Hello ${complaint.user.firstName} ${complaint.user.lastName}!
New complaint <#if complaint.productInstance??>regarding product "${complaint.productInstance.price.product.productName}" </#if>by reason of "${complaint.complainReason.categoryName}" successfully created.

Complaint subject:

${complaint.complainTitle}
<#if complaint.content??>

Content:

${complaint.content}.
</#if>