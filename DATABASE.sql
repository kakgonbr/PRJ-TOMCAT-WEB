CREATE TABLE tblResourceMap
(
	id varchar(30) PRIMARY KEY,
	systemPath varchar(50) NOT NULL
)

CREATE TABLE tblUser
(
	id int PRIMARY KEY IDENTITY(1, 1),
	email nvarchar(50) NOT NULL UNIQUE,
	username varchar(30) NOT NULL UNIQUE,
	phoneNumber varchar(12) not null UNIQUE,
	password varchar(50) NOT NULL,
	persistentCookie varchar(255),
	googleId varchar(255),	
	facebookId varchar(255),
	isAdmin bit DEFAULT 0,
	credit money,
	status bit,

	displayName nvarchar(50),
	profileStringResourceId varchar(30),
	bio nvarchar(255),

	CONSTRAINT fk_user_resourceId FOREIGN KEY (profileStringResourceId) REFERENCES tblResourceMap(id)
)

CREATE TABLE tblPreference
(
	id int PRIMARY KEY IDENTITY(1, 1),
	userId int NOT NULL,
	keyword varchar(30) NOT NULL,

	CONSTRAINT fk_pref_user FOREIGN KEY (userId) REFERENCES tblUser
)

CREATE TABLE tblUserPreference
(
	userId int,
	preference nvarchar(30),

	PRIMARY KEY (userId, preference),
	CONSTRAINT fk_userpref_user FOREIGN KEY (userId) REFERENCES tblUser(id)
)

CREATE TABLE tblChatBox
(
	id int PRIMARY KEY IDENTITY(1, 1),
	user1 int,
	user2 int,

	CONSTRAINT fk_chatbox_user1 FOREIGN KEY (user1) REFERENCES tblUser(id),
	CONSTRAINT fk_chatbox_user2 FOREIGN KEY (user2) REFERENCES tblUser(id)
)

CREATE TABLE tblChatBoxContent
(
	id int PRIMARY KEY IDENTITY(1, 1),
	chatBoxId int,
	message nvarchar(100) NOT NULL,
	time DATETIME NOT NULL DEFAULT GETDATE(),
	senderId int,

	CONSTRAINT fk_cbcontent_sender FOREIGN KEY (senderId) REFERENCES tblUser(id),
	CONSTRAINT fk_cbcontent_chatbox FOREIGN KEY (chatBoxId) REFERENCES tblChatBox(id)
)

CREATE TABLE tblPromotion
(
	id int PRIMARY KEY IDENTITY(1, 1),
	creatorId int,
	name nvarchar(30) NOT NULL,
	type bit DEFAULT 0, -- 0 is %, 1 is flat
	ofAdmin bit DEFAULT 0, 
	value int NOT NULL,
	creationDate DATE DEFAULT GETDATE(),
	expireDate DATE NOT NULL,

	CONSTRAINT fk_promo_creator FOREIGN KEY (creatorId) REFERENCES tblUser(id)
)

CREATE TABLE tblShop
(
	id int PRIMARY KEY IDENTITY(1, 1),
	ownerId int,
	name nvarchar(30) NOT NULL,
	address nvarchar(100),
	profileStringResourceId varchar(30),
	visible bit DEFAULT 0,

	CONSTRAINT fk_shop_resourceId FOREIGN KEY (profileStringResourceId) REFERENCES tblResourceMap(id),
	CONSTRAINT fk_shop_owner FOREIGN KEY (ownerId) REFERENCES tblUser(id)
)

CREATE TABLE tblCategory
(
	id int PRIMARY KEY IDENTITY(1, 1),
	name nvarchar(30),
	imageStringResourceId varchar(30),
	parent_id int

	CONSTRAINT fk_category_resourceId FOREIGN KEY (imageStringResourceId) REFERENCES tblResourceMap(id),
	CONSTRAINT fk_category_parentId FOREIGN KEY (parent_id) REFERENCES tblCategory(id)
)

CREATE TABLE tblVariation
(
	id int PRIMARY KEY IDENTITY(1, 1),
	categoryId int,
	name nvarchar(30),
	datatype varchar(10),
	unit varchar(10),

	CONSTRAINT fk_variation_category FOREIGN KEY (categoryId) REFERENCES tblCategory(id)
)

CREATE TABLE tblVariationValue
(
	id int PRIMARY KEY IDENTITY(1, 1),
	variationId int,
	value varchar(15),

	constraint fk_variationvalue_variation foreign key (variationId) references tblVariation(id)
)

CREATE TABLE tblProduct
(
	id int PRIMARY KEY IDENTITY(1, 1),
	shopId int,
	categoryId int,
	name nvarchar(50) NOT NULL,
	description nvarchar(255),
	availablePromotionId int,
	imageStringResourceId varchar(30),
	status bit,
	
	CONSTRAINT fk_product_resourceId FOREIGN KEY (imageStringResourceId) REFERENCES tblResourceMap(id),
	CONSTRAINT fK_product_shop FOREIGN KEY (shopId) REFERENCES tblShop(id),
	CONSTRAINT fk_product_category FOREIGN KEY (categoryId) REFERENCES tblCategory(id),
	CONSTRAINT fk_product_promo FOREIGN KEY (availablePromotionId) REFERENCES tblPromotion(id)
)

CREATE TABLE tblProductImage 
(
	id int PRIMARY KEY IDENTITY(1, 1),
	productId int,
	imageStringResourceId varchar(30),

	CONSTRAINT fk_productimage_resourceId FOREIGN KEY (imageStringResourceId) REFERENCES tblResourceMap(id),
	CONSTRAINT fk_productimage_product FOREIGN KEY (productId) REFERENCES tblProduct
)

create table tblProductItem
(
	id int PRIMARY KEY IDENTITY(1, 1),
	productId int,
	stock int,
	price money,

	constraint fk_productitem_product foreign key (productId) references tblProduct(id)
)

create table tblProductCustomization
(
	id int PRIMARY KEY IDENTITY(1, 1),
	productItemId int,
	variationValueId int,

	constraint fk_productcustomization_productitem foreign key (productItemId) references tblProductItem(id),
	constraint fk_productcustomization_variationvalue foreign key (variationValueId) references tblVariationValue(id)
)

create table tblReview
(
	id int PRIMARY KEY IDENTITY(1, 1),
	userId int,
	productId int,
	rate int,
	comment nvarchar(255),
	status bit,

	constraint fk_review_user FOREIGN KEY (userId) REFERENCES tblUser(id),
	constraint fk_review_product FOREIGN KEY (productId) REFERENCES tblProduct(id)
)

CREATE TABLE tblCart
(
	id int PRIMARY KEY IDENTITY(1, 1),
	userId int,

	CONSTRAINT fk_cart_user FOREIGN KEY (userId) REFERENCES tblUser(id)
)

CREATE TABLE tblCartItem
(
	id int PRIMARY KEY IDENTITY(1, 1),
	cartId int,
	productItemId int,
	quantity int NOT NULL,
	status bit DEFAULT 0,

	-- CONSTRAINT pk_cartitem PRIMARY KEY (cartId, productCustomizationId),
	CONSTRAINT fk_cartitem_cart FOREIGN KEY (cartId) REFERENCES tblCart(id),
	CONSTRAINT fk_cartitem_productitem FOREIGN KEY (productItemId) REFERENCES tblProductItem(id)
)

CREATE TABLE tblPaymentMethod
(
	id int PRIMARY KEY IDENTITY(1, 1),
	name varchar(30)
)

CREATE TABLE tblOrder
(
	id int PRIMARY KEY IDENTITY(1, 1),
	userId int,
	paymentMethodId int,
	shippingAddress nvarchar(100),
	promotionId int,
	date datetime DEFAULT GETDATE(),
	finalPrice money,  --after promotion and iclude shipping cost
	status bit DEFAULT 0,


	CONSTRAINT fk_order_user FOREIGN KEY (userId) REFERENCES tblUser(id),
	CONSTRAINT fK_order_paymentMethod FOREIGN KEY (paymentMethodId) REFERENCES tblPaymentMethod(id),
	CONSTRAINT fk_order_promotion FOREIGN KEY (promotionId) REFERENCES tblPromotion(id)
)

CREATE TABLE tblOrderedItem
(
	id int PRIMARY KEY IDENTITY(1, 1),
	orderId int,
	productItemId int,
	orderStatus varchar(30),
	quantity int NOT NULL,
	totalPrice money,
	shippingCost money, --per shop per order

	CONSTRAINT fk_ordereditem_order FOREIGN KEY (orderId) REFERENCES tblOrder(id),
	CONSTRAINT fk_ordereditem_productitem FOREIGN KEY (productItemId) REFERENCES tblProductItem(id)
)

CREATE TABLE tblOnholdCredit
(
	id int PRIMARY KEY IDENTITY(1, 1),
	userId int,
	amount money NOT NULL,
	date date DEFAULT GETDATE(),
	claimDate date DEFAULT DATEADD(day, 7, GETDATE()),

	CONSTRAINT fk_onhold_user FOREIGN KEY (userId) REFERENCES tblUser(id)
)

CREATE TABLE tblNotification
(
	id int PRIMARY KEY IDENTITY(1, 1),
	userId int, -- can be null if the notification is global
	title nvarchar(30) NOT NULL,
	body nvarchar(255),
	isRead bit DEFAULT 0,

	CONSTRAINT fk_notification_user FOREIGN KEY (userId) REFERENCES tblUser
)

CREATE TABLE tblServerStatistics
(
	id int PRIMARY KEY IDENTITY(1, 1), -- stats wont be deleted
	day date NOT NULL,
	totalMoneyEarned int,
	userNum int,
	productNum int,
	shopNum int,
	promotionNum int,
	purchaseNum int,
	visitNum int,
	peakSessionNum int,
	averageResponseTime int, -- in milisec
	maxResponseTime int
)

CREATE TABLE tblShopStatistics
(
	id int PRIMARY KEY IDENTITY(1,1),
	shopId int,
	CONSTRAINT fk_shopstatistics_id FOREIGN KEY (shopId) references tblShop(id)
)

-- TF-IdF RELATED STUFF
CREATE TABLE tblBaseVector 
(
    keyword nvarchar(MAX)
);

CREATE TABLE tblVector (
    productId int PRIMARY KEY,
    vector nvarchar(MAX)
);

GO
CREATE PROCEDURE ComputeTFIdF
AS
BEGIN
	DELETE FROM tblVector;
	DELETE FROM tblBaseVector;

	INSERT INTO tblBaseVector (keyword)
	(
		SELECT DISTINCT TRIM(value) AS keyword 
		FROM tblProduct
		CROSS APPLY STRING_SPLIT(LOWER(tblProduct.name), ' ')
		WHERE LEN(value) > 0

		UNION

		SELECT DISTINCT TRIM(value) AS keyword
		FROM tblProduct
		CROSS APPLY STRING_SPLIT(LOWER(tblProduct.description), ' ')
		WHERE LEN(value) > 0
	);

	WITH tfbefore AS (
		SELECT 
			p.id, 
			TRIM(s.value) AS keyword, 
			COUNT(*) * 0.7 / NULLIF((SELECT COUNT(*) FROM STRING_SPLIT(LOWER(p.name), ' ')), 0) AS tf
		FROM tblProduct p
		CROSS APPLY STRING_SPLIT(LOWER(p.name), ' ') s
		GROUP BY p.id, s.value, p.name

		UNION ALL

		SELECT 
			p.id, 
			TRIM(s.value) AS keyword, 
			COUNT(*) * 0.3 / NULLIF((SELECT COUNT(*) FROM STRING_SPLIT(LOWER(p.description), ' ')), 0) AS tf
		FROM tblProduct p
		CROSS APPLY STRING_SPLIT(LOWER(p.description), ' ') s
		GROUP BY p.id, s.value, p.description
	),
	tf_combined AS (
		SELECT 
			id, 
			keyword, 
			SUM(tf) AS tf 
		FROM tfbefore
		GROUP BY id, keyword
	),
	tf AS (
		SELECT b.id, a.keyword AS keyword, COALESCE(b2.tf, 0) tf
		FROM (SELECT DISTINCT keyword FROM tblBaseVector) a
		CROSS JOIN (SELECT DISTINCT id FROM tf_combined) b
		LEFT JOIN tf_combined b2 ON b.id = b2.id AND a.keyword = b2.keyword
	),
	df AS (
		SELECT keyword, COUNT(DISTINCT id) AS DF FROM tf_combined GROUP BY keyword
	),
	tfidf AS (
		SELECT 
			tf.id, 
			tf.keyword, 
			tf.TF * LOG((SELECT COUNT(*) FROM tblProduct) / NULLIF(df.DF, 0)) AS TFIdF
		FROM tf
		JOIN df ON tf.keyword = df.keyword
	),
	vectors AS (
		SELECT 
			id, 
			STRING_AGG(CAST(COALESCE(tfidf, 0) AS NVARCHAR), ',') WITHIN GROUP (ORDER BY tblBaseVector.keyword) AS Vector
		FROM tblBaseVector
		LEFT JOIN tfidf ON tblBaseVector.keyword = tfidf.keyword AND tfidf.id = id  -- Fixed join condition
		GROUP BY id
	)
	INSERT INTO tblVector
	SELECT * FROM vectors;
END;
GO

GO
CREATE PROCEDURE GetRecommendation (@query NVARCHAR(400), @page int)
AS
BEGIN
	SET NOCOUNT ON;

	IF @query IS NULL OR LTRIM(RTRIM(@query)) = ''
    BEGIN
        SELECT TOP 10 * FROM tblProduct ORDER BY NEWId();
        RETURN;
    END

    CREATE TABLE #result (value varchar(400));
    
    WITH tf AS (
		SELECT b.keyword, COUNT(s.value) * 1.0 / NULLIF((SELECT COUNT(*) FROM STRING_SPLIT(@query, ' ')), 0) AS TF
        FROM STRING_SPLIT(@query, ' ') s
        RIGHT JOIN tblBaseVector b ON b.keyword = s.value
        GROUP BY b.keyword
	),
	df AS (
    --SELECT s.value AS keyword, COUNT(*) AS DF FROM STRING_SPLIT(@query, ' ') s GROUP BY s.value
	SELECT keyword, COUNT(DISTINCT id) AS DF 
    FROM (
        SELECT 
            id,
            TRIM(s.value) AS keyword
        FROM tblProduct
        CROSS APPLY STRING_SPLIT(LOWER(tblProduct.description), ' ') s
        
        UNION
        
        SELECT 
            id,
            TRIM(s.value) AS keyword
        FROM tblProduct
        CROSS APPLY STRING_SPLIT(LOWER(tblProduct.name), ' ') s
    ) tbl
    GROUP BY keyword
	),
	tfidf AS (
		SELECT 
			tf.keyword, 
			tf.TF * LOG((SELECT COUNT(*) FROM tblProduct) / NULLIF(df.DF, 0)) AS TFIdF
		FROM tf
		JOIN df ON tf.keyword = df.keyword
	),
	vectors AS (
    SELECT
        STRING_AGG(CAST(COALESCE(tfidf, 0) AS NVARCHAR), ',') WITHIN GROUP (ORDER BY tblBaseVector.keyword) AS vector
    FROM tblBaseVector
    LEFT JOIN tfidf ON tblBaseVector.keyword = tfidf.keyword AND tblBaseVector.keyword = tfidf.keyword
	)
	INSERT INTO #result
    SELECT * FROM vectors;

    --SELECT * FROM STRING_SPLIT((SELECT value FROM #result), ',');

	WITH ItemVector AS (
		SELECT v.productId AS id, s.value AS tfidf_value, 
           ROW_NUMBER() OVER (PARTITION BY v.productId ORDER BY (SELECT NULL)) AS pos
		FROM tblVector v
		CROSS APPLY STRING_SPLIT(v.vector, ',') s
	),
	QueryVector AS (
		SELECT s.value AS tfidf_value, ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS pos
		FROM STRING_SPLIT((SELECT value FROM #result), ',') s
	),
	recommendation AS(
		SELECT iv.id,
		SUM(CAST(qv.tfidf_value AS FLOAT) * CAST(iv.tfidf_value AS FLOAT)) /
		NULLIF((SQRT(SUM(POWER(CAST(iv.tfidf_value AS FLOAT), 2))) * SQRT(SUM(POWER(CAST(qv.tfidf_value AS FLOAT), 2)))), 0) AS similarity
		FROM ItemVector iv
		JOIN QueryVector qv ON iv.pos = qv.pos
		GROUP BY iv.id
	)
	SELECT tblProduct.*
	FROM tblProduct
	JOIN recommendation ON tblProduct.id = recommendation.id
	WHERE recommendation.similarity != 0
	ORDER BY recommendation.similarity DESC
	OFFSET @page * 10 ROWS 
    FETCH NEXT 10 ROWS ONLY

    DROP TABLE #result;
END;
GO

INSERT INTO tblResourceMap
VALUES
('test_js', 'test.js'),
('chat_js', 'chat.js'),
('admin_js', 'admin.js'),
('admin_css', 'admin.css'),
('log_js', 'log.js'),
('product_js', 'product.js'),
('chart_js', 'customChart.js'),
('userMain_css', 'userMain.css'),
('login_css', 'login.css');

INSERT INTO tblUser (email, username, phoneNumber, password, persistentCookie, googleId, facebookId, isAdmin)
VALUES 
('abc@example.com', 'user', '00000', 'user', NULL, NULL, NULL, 0),
('abc1@example.com', 'admin', '00001', 'admin', NULL, NULL, NULL, 1),
('abc2@example.com', 'user123', '05602', 'user', NULL, NULL, NULL, 0),
('abc3@example.com', 'user33443', '12003', 'user', NULL, NULL, NULL, 0),
('abc4@example.com', 'user126543', '56004', 'user', NULL, NULL, NULL, 0);


INSERT INTO tblShop (ownerId, name, address, profileStringResourceId, visible)
VALUES
(1, 'Gadget World', '789 Tech Road', 'chart_js', 1),
(2, 'Sneaker Haven', '321 Fashion Blvd', 'admin_css', 1),
(3, 'Home Essentials', '567 Home Lane', 'admin_js', 1),
(4, 'Tech Universe', '123 Innovation St', 'test_js', 1),
(5, 'Fashion Hub', '456 Style Ave', 'admin_css', 1);


/*parent id is for testing -> change when real*/
INSERT INTO tblCategory (name, imageStringResourceId, parent_id)
VALUES
	('Fashion','chart_js',NULL), --1
	('Electronics','chart_js',NULL), --2
	('Furniture','chart_js',NULL), --3
	('Cosmestic','chart_js',NULL), --4
	('Book','chart_js',NULL), --5
	('Man Fashion','chart_js',1), --6
	('Woman Fashion','chart_js',1), --7
	('Shoes','chart_js',1), --8
	('Accessory','chart_js',1), --9
	('T-Shirt','chart_js',6),
	('Blazer','chart_js',6),
	('Hoodie','chart_js',6),
	('Shirt','chart_js',6),
	('Jacket','chart_js',6),
	('Coat','chart_js',6),
	('Polo Shirt','chart_js',6),
	('Man Jean','chart_js',6),
	('Short','chart_js',6),
	('Trouser','chart_js',6),
	('skincare','chart_js',4), --20
	('makeup','chart_js',4), --21
	('haircare','chart_js',4), --22
	('bodycare','chart_js',4), --23
	('fragrance','chart_js',4), --24
	('cleansers', 'chart_js', 20),
    ('face_wash', 'chart_js', 20),
    ('makeup_remover', 'chart_js', 20),
    ('toners', 'chart_js', 20),
    ('moisturizers', 'chart_js', 20),
    ('serums', 'chart_js', 20),
    ('eye_care', 'chart_js', 20),
    ('sunscreen', 'chart_js', 20),
    ('face_masks', 'chart_js', 20) ;

INSERT INTO tblVariation (categoryId,name,datatype,unit)
VALUES 
	(1,'Color','String',NULL),
	(6,'Man Clothes Size','String',NULL),
	(8,'Shoe Size','String',NULL),
	(7,'Woman Clothes Size','String',NULL);

INSERT INTO tblVariationValue (variationId, value)
VALUES 
	(1 , 'Black'),
	(1 , 'White'),
	(1 , 'Gray'),
	(1 , 'Blue'),
	(1 , 'Red'),
	(1 , 'Beige'),
	(1 , 'Brown'),
	(1 , 'Navy'),
	(1 , 'Pink'),
	(1 , 'Orange'),
	(1 , 'Green'),
	(1 , 'Yellow'),
	(2 , 'XS'),
	(2 , 'S'),
	(2 , 'M'),
	(2 , 'L'),
	(2 , 'XL'),
	(2 , 'XXL'),
	(3 , '35'),
	(3 , '36'),
	(3 , '37'),
	(3 , '38'),
	(3 , '39'),
	(3 , '40'),
	(3 , '41'),
	(3 , '42'),
	(3 , '43'),
	(4 , 'XS'),
	(4 , 'S'),
	(4 , 'M'),
	(4 , 'L'),
	(4 , 'XL');

	-- skincare
	

	-- makeup 
    ('foundation', 'chart_js', 37),
    ('concealer', 'chart_js', 37),
    ('powder', 'chart_js', 37),
    ('blush', 'chart_js', 37),
    ('contour', 'chart_js', 37),
    ('highlighter', 'chart_js', 37),
    ('lipstick', 'chart_js', 37),
    ('lip gloss', 'chart_js', 37),
    ('lip liner', 'chart_js', 37),
    ('eyeshadow', 'chart_js', 37),
    ('eyeliner', 'chart_js', 37),

	-- Haircare 
    ('shampoo', 'chart_js', 38),
    ('conditioner', 'chart_js', 38),
    ('dry_shampoo', 'chart_js', 38),
    ('hair oil', 'chart_js', 38),
    ('hair mask', 'chart_js', 38),
    ('hair treatment', 'chart_js', 38),
    ('heat protectant', 'chart_js', 38),
    ('hair spray', 'chart_js', 38),

	-- Bodycare 
    ('body wash', 'chart_js', 39),
    ('body scrub', 'chart_js', 39),
    ('body lotion', 'chart_js', 39),
    ('body butter', 'chart_js', 39),
    ('hand cream', 'chart_js', 39),
    ('foot cream', 'chart_js', 39),

	-- Fragrance 
    ('perfume', 'chart_js', 40),
    ('eau de toilette', 'chart_js', 40),
    ('body mist', 'chart_js', 40),
    ('essential oil', 'chart_js', 40);

	
EXEC ComputeTFIdF
