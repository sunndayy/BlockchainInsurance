const express = require('express');
const router = express.Router();
const multer = require('multer');
const upload = multer({dest: 'uploads/'});
const userMiddleware = require('../../../middleware/user-middleware');
const productController = require('../controller/product-controller');

router.get('/products', productController.getAllProducts);

router.get('/product-image/:id', productController.getProductImage);

router.get('/products-by-types/:type', productController.getProductsByType);

router.get('/products-by-producer/:producer', productController.getProductsByProducer);

router.post('/create-product', userMiddleware.authMiddleware, upload.single('image'), productController.createProduct);

router.put('/update-product/:id', userMiddleware.authMiddleware, upload.single('image'), productController.updateProduct);

router.get('/products/best-sellers', productController.getBestSellers);

router.get('/products-by-keyword/:keyword', productController.getProductsByKeyword);

router.post('/like-product', userMiddleware.authMiddleware, productController.likeProduct);

router.post('/unlike-product', userMiddleware.authMiddleware, productController.unlikeProduct);

router.get('/favorite-products', userMiddleware.authMiddleware, productController.getFavoriteProducts);

module.exports = router;