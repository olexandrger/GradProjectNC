<#-- @ftlvariable name="order" type="com.grad.project.nc.model.ProductOrder" -->
Hello ${order.user.firstName} ${order.user.lastName}!

New order ${order.orderAim.categoryName} ${order.productInstance.price.product.productName} #${order.productOrderId} successfully created.