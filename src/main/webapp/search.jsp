<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

	<html>

	<head>
		<meta http-equiv="content-type" content="text/html; charset=utf-8">
		<meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no">
		<title>Search</title>
		<link rel="icon" href="./search_line_icon.svg">
		<link rel="stylesheet" href="${pageContext.request.contextPath}/css/cool.css?<?php echo time(); ?">
	</head>

	<body>
		<header>
			<nav>
				<ul class="header-nav">

				</ul>

			</nav>

		</header>
		<section>
			<h1>Find Everything</h1>
			<form action="searchWeb" method="post">
				<select name="SearchRange" class="form-control info-select"
						onChange="set_city(this, this.form.MultiField);">
					<option value="0" selected="selected">全域搜索</option>
					<option value="Normal">普通搜索</option>
					<option value="Wildcard">通配符搜索</option>
					<option value="Fuzzy">模糊搜索</option>
					<option value="Span">跨度搜索</option>
					<option value="Regexp">正则搜索</option>
				</select>

				<select name="MultiField" class="form-control info-select">
					<option value="0" disabled="disabled"> -- </option>
				</select>

				<input type="text" id="search" value="Search Academic Papers"
					onfocus="if(value=='Search Academic Papers')value=''"
					onblur="if(!value)value='Search Academic Papers'" name="queryString" class="searchbar">
				<input type="image" src="search_line_icon.svg" name="submit" class="mg">
			</form>
			<!-- 当选择跨度搜索时，显示： -->
			<div class="form-row" id="span-form" style="display: none;">
				请按序输入起始词、结尾词、是否有序（True/False，若True则起始词一定在结尾词前）、两词之间最大间隔、排除词，中间以逗号间隔
			</div>
		</section>
	</body>

	<script>
		field = [];
		field['Normal'] = ['Title', 'Author', 'Affiliation', 'Address', 'Date', 'Abstract&keywords', 'MainText', 'Acknowledgement&Reference'];
		field['Wildcard'] = ['Title', 'Author', 'Affiliation', 'Address', 'Date', 'Abstract&keywords', 'MainText', 'Acknowledgement&Reference'];
		field['Fuzzy'] = ['Title', 'Author', 'Affiliation', 'Address', 'Date', 'Abstract&keywords', 'MainText', 'Acknowledgement&Reference'];
		field['Span'] = ['Title', 'Author', 'Affiliation', 'Address', 'Date', 'Abstract&keywords', 'MainText', 'Acknowledgement&Reference'];
		field['Regexp'] = ['Title', 'Author', 'Affiliation', 'Address', 'Date', 'Abstract&keywords', 'MainText', 'Acknowledgement&Reference'];

		function set_city(SearchRange, MultiField) {
			let pv, cv;
			let i, ii;

			pv = SearchRange.value;
			cv = MultiField.value;

			MultiField.length = 1;

			if (pv == '0') return;
			if (typeof (field[pv]) == 'undefined') return;

			for (i = 0; i < field[pv].length; i++) {
				ii = i + 1 ;
				MultiField.options[ii] = new Option();
				MultiField.options[ii].text = field[pv][i];
				MultiField.options[ii].value = field[pv][i];
			}

			const select = document.querySelector('#SearchRange')
			const ref = document.querySelector('#span-form')
			if (select.value === 'Span') {
				ref.style.display = 'block'
			}
		}


	</script>

	</html>