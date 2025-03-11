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
    keyword nvarchar(MAX) PRIMARY KEY
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
CREATE PROCEDURE GetRecommendation (@query NVARCHAR(400))
AS
BEGIN
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
	SELECT keyword, COUNT(DISTINCT id) AS DF FROM (SELECT 
													id,
													TRIM(s.value) AS keyword
													FROM tblProduct
													CROSS APPLY STRING_SPLIT(LOWER(tblProduct.description), ' ') s
													GROUP BY tblProduct.id, s.value, tblProduct.description) tbl
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
		SELECT v.productId, s.value AS tfidf_value, 
           ROW_NUMBER() OVER (PARTITION BY v.productId ORDER BY (SELECT NULL)) AS pos
		FROM tblVector v
		CROSS APPLY STRING_SPLIT(v.vector, ',') s
	),
	QueryVector AS (
		SELECT s.value AS tfidf_value, ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS pos
		FROM STRING_SPLIT((SELECT value FROM #result), ',') s
	),
	recommendation AS(
		SELECT TOP 10 iv.id,
		SUM(CAST(qv.tfidf_value AS FLOAT) * CAST(iv.tfidf_value AS FLOAT)) /
		(SQRT(SUM(POWER(CAST(iv.tfidf_value AS FLOAT), 2))) * SQRT(SUM(POWER(CAST(qv.tfidf_value AS FLOAT), 2)))) AS similarity
		FROM ItemVector iv
		JOIN QueryVector qv ON iv.pos = qv.pos
		GROUP BY iv.id
	)
	SELECT tblProduct.*
	FROM tblProduct
	JOIN recommendation ON tblProduct.id = recommendation.id
	ORDER BY recommendation.similarity DESC

    DROP TABLE #result;
END;
GO

INSERT INTO tblResourceMap
VALUES
('test_js', 'test.js'),
('chat_js', 'chat.js'),
('admin_js', 'admin.js'),
('admin_css', 'admin.css'),
('chart_js', 'customChart.js');

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
select * from tblShop

INSERT INTO tblCategory (name, imageStringResourceId, parent_id)
VALUES
('Smartphones', 'chart_js', 1),
('Laptops', 'chart_js', 1),
('Footwear', 'admin_css', 2),
('Accessories', 'admin_css', 2),
('Home Appliances', 'admin_js', NULL),
('Gaming Consoles', 'test_js', NULL),
('Furniture', 'admin_js', NULL),
('Electronics', 'chart_js', NULL),
('Clothing', 'admin_css', NULL);

INSERT INTO tblPromotion (creatorId, name, type, ofAdmin, value, expireDate)
VALUES
(1, 'Black Friday Sale', 0, 0, 15, '2025-11-29'),
(3, 'New Year Offer', 1, 0, 75000, '2025-12-31'),
(2, 'Buy 1 Get 1', 0, 1, 50, '2025-12-31'),
(4, 'Summer Discount', 0, 0, 20, '2025-06-30'),
(5, 'Winter Clearance', 1, 1, 10000, '2025-12-15');


INSERT INTO tblProduct (shopId, categoryId, name, description, availablePromotionId, imageStringResourceId, status)
VALUES
(4, 3, 'Samsung Galaxy S22', 'Flagship Samsung smartphone', 1, 'test_js', 1),
(4, 4, 'MacBook Pro 14', 'Apple high-end laptop', 2, 'test_js', 1),
(5, 5, 'Nike Air Max', 'Stylish and comfortable sneakers', 3, 'admin_css', 1),
(5, 6, 'Leather Handbag', 'Elegant leather handbag', NULL, 'admin_css', 1),
(6, 7, 'Air Fryer', 'Healthy cooking appliance', NULL, 'admin_js', 1),
(6, 7, 'Vacuum Cleaner', 'Powerful home cleaning device', NULL, 'admin_js', 1),
(7, 8, 'PlayStation 5', 'Next-gen gaming console', 4, 'test_js', 1),
(7, 8, 'Xbox Series X', 'Powerful Microsoft gaming console', 4, 'test_js', 1),
(8, 9, 'Modern Sofa', 'Comfortable and stylish', 5, 'admin_js', 1),
(8, 9, 'Wooden Dining Table', 'Elegant and durable', 5, 'admin_js', 1),
(4, 3, 'iPhone 14 Pro', 'Latest Apple smartphone', 1, 'test_js', 1),
(4, 5, 'Adidas Ultraboost', 'High-performance running shoes', 3, 'admin_css', 1),
(6, 7, 'Microwave Oven', 'Efficient and modern', NULL, 'admin_js', 1),
(7, 8, 'Nintendo Switch', 'Portable gaming console', 4, 'test_js', 1),
(8, 9, 'Queen Size Bed', 'Luxurious and comfortable', 5, 'admin_js', 1),
(4, 3, 'Google Pixel 7', 'Latest Google smartphone', 1, 'test_js', 1),
(5, 5, 'Puma Running Shoes', 'Lightweight and stylish', 3, 'admin_css', 1),
(6, 7, 'Blender', 'Powerful kitchen appliance', NULL, 'admin_js', 1),
(7, 8, 'Gaming Laptop', 'High-end gaming performance', 4, 'test_js', 1),
(8, 9, 'Office Chair', 'Ergonomic and comfortable', 5, 'admin_js', 1),
(4, 3, 'OnePlus 11', 'Flagship OnePlus smartphone', 1, 'test_js', 1),
(5, 5, 'Reebok Sneakers', 'Durable and comfortable', 3, 'admin_css', 1),
(6, 7, 'Dishwasher', 'Efficient and modern', NULL, 'admin_js', 1),
(7, 8, 'Smart TV', '4K Ultra HD', 4, 'test_js', 1),
(8, 9, 'Bookshelf', 'Modern wooden bookshelf', 5, 'admin_js', 1);


INSERT INTO tblProductItem (productId, stock, price)
VALUES
(5, 12, 1100),
(6, 8, 2000),
(7, 30, 250),
(8, 15, 180),
(9, 10, 300),
(10, 5, 500),
(11, 25, 1300),
(12, 20, 220),
(13, 18, 400),
(14, 10, 450),
(15, 8, 550),
(16, 12, 350),
(17, 5, 700),
(18, 6, 600),
(19, 7, 1200),
(20, 4, 1500),
(21, 15, 750),
(22, 20, 300),
(23, 10, 850),
(24, 5, 1800),
(25, 8, 950),
(26, 12, 500),
(2, 18, 650),
(3, 6, 1400),
(4, 9, 800);

