GET /get-header: Trả về header cửa block theo index hoặc hash
POST /header: Kiểm tra header có hợp lệ hay không, nếu hợp lệ gửi yêu cầu lấy data (dựa trên pubKeyHash để quyết định URL)
GET /get-data: Trả về data của block theo index
POST /data: Kiểm tra data có hợp lệ hay không, nếu hợp lệ gửi yêu cầu lấy header tiếp theo (dựa trên pubKeyHash để quyết định URL)
POST /validate: Xác nhận 1 block