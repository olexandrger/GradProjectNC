<!DOCTYPE html>
<html lang="en">
<head>
<#import "spring.ftl" as spring />
</head>
<body>
<div>
    <div>
        <form method="POST">
        <input name="${_csrf.parameterName}" value="${_csrf.token}" type="hidden">
        <@spring.bind "user" />
            <h2>Create your account</h2>
            <@spring.formInput "user.email"/>
            <@spring.formInput "user.password" />
            <input type="submit" value="submit" />
        </form>
    </div>


</div>
</body>
</html>