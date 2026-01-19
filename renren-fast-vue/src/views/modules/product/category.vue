<template>
    <div>
        <el-switch v-model="draggable" active-text="开启拖拽" inactive-text="关闭拖拽"></el-switch>
        <el-button type="danger" @click="batchDelete">批量删除</el-button>
        <el-tree :data="menus" :props="defaultProps" @node-click="handleNodeClick" show-checkbox node-key="catId"
            :default-expanded-keys="expandedKey" :draggable="draggable" :allow-drop="allowDrop" @node-drop="handleDrop"
            ref="menuTree">
            <span class="custom-tree-node" slot-scope="{ node, data }">
                <span>{{ node.label }}</span>
                <span>
                    <el-button type="text" size="mini" @click="() => append(data)" v-if="node.level <= 2">
                        增加
                    </el-button>
                    <el-button type="text" size="mini" @click="() => remove(node, data)"
                        v-if="node.childNodes.length === 0">
                        删除
                    </el-button>
                    <el-button type="text" size="mini" @click="() => edit(data)">
                        修改
                    </el-button>
                </span>
            </span>
        </el-tree>
        <el-dialog :title="title" :visible.sync="dialogVisible" width="30%" :close-on-click-modal="false">
            <el-form :model="category">
                <el-form-item label="分类名称" :label-width="formLabelWidth">
                    <el-input v-model="category.name" autocomplete="off"></el-input>
                </el-form-item>
            </el-form>
            <el-form :model="category">
                <el-form-item label="图标" :label-width="formLabelWidth">
                    <el-input v-model="category.icon" autocomplete="off"></el-input>
                </el-form-item>
            </el-form>
            <el-form :model="category">
                <el-form-item label="计量单位" :label-width="formLabelWidth">
                    <el-input v-model="category.productUnit" autocomplete="off"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="submitData">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
export default {
    data() {
        return {
            draggable: false,
            updateNodes: [],
            title: "",
            dialogType: "",
            dialogVisible: false,
            menus: [],
            expandedKey: [],
            defaultProps: {
                children: "children",
                label: "name"
            },
            category: {
                name: "",
                parentCid: 0,
                catLevel: 0,
                showStatus: 1,
                sort: 0,
                catId: null,
                productUnit: "",
                icon: "",
                productCount: 0
            }
        };
    },
    methods: {
        handleNodeClick(data) {
            console.log(data);
        },
        getMenus() {
            this.$http({
                url: this.$http.adornUrl('/product/category/list/tree'),
                method: 'get'
            }).then(({ data }) => {
                console.log("后端返回的数据:", data.data);
                if (data.data && data.code === 200) {
                    this.menus = data.data;
                } else {
                    this.$message.error("获取菜单失败");
                }
            });
        },
        append(data) {
            this.dialogType = "add";
            this.title = "添加分类";
            this.dialogVisible = true;


            this.category = {
                name: "",
                parentCid: data.catId,
                catLevel: (data.catLevel || 0) + 1,
                showStatus: 1,
                sort: 0,
                catId: null,
                icon: "",
                productUnit: ""
            };
        },
        remove(node, data) {
            var ids = [data.catId];
            this.$confirm(`是否删除【${data.name}】菜单?`, '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.$http({
                    url: this.$http.adornUrl('/product/category/delete'),
                    method: 'delete',
                    data: this.$http.adornData(ids, false)
                }).then(({ data }) => {
                    if (data.code === 200) {
                        this.$message.success("菜单删除成功");
                        this.getMenus();
                        this.expandedKey = [node.parent.data.catId];
                    } else {
                        this.$message.error("菜单删除失败: " + data.msg);
                    }
                })
            }).catch(() => {
            });


        },
        addCategory() {
            console.log("添加的分类数据:", this.category);
            this.dialogVisible = false;
            console.log("发送添加分类请求");
            this.$http({
                url: this.$http.adornUrl('/product/category/save'),
                method: 'post',
                data: this.$http.adornData(this.category, false)
            }).then(({ data }) => {
                if (data.code === 200) {
                    this.$message.success("分类添加成功");
                    this.getMenus();
                    this.expandedKey = [this.category.parentCid];
                    this.category.name = "";
                } else {
                    this.$message.error("分类添加失败: " + data.msg);
                }
            });
            console.log("添加分类请求发送完毕");
        },
        submitData(data) {
            if (this.dialogType === "add") {
                this.addCategory();
            } else if (this.dialogType === "edit") {
                this.editCategory();
            }
        },
        edit(data) {
            console.log("编辑分类数据:", data);
            this.dialogType = "edit";
            this.title = "修改分类";
            this.dialogVisible = true;

            this.$http({
                url: this.$http.adornUrl(`/product/category/info/${data.catId}`),
                method: 'get'
            }).then(({ data }) => {
                if (data.code === 200) {
                    console.log("获取分类信息成功:", data.data);
                    this.category = data.data;
                } else {
                    this.$message.error("获取分类信息失败: " + data.msg);
                }
            });
        },
        editCategory() {
            console.log("修改的分类数据:", this.category);
            this.dialogVisible = false;
            console.log("发送修改分类请求");

            let { catId, name, icon, productUnit } = this.category;

            this.$http({
                url: this.$http.adornUrl('/product/category/update'),
                method: 'put',
                data: this.$http.adornData({ catId, name, icon, productUnit }, false)
            }).then(({ data }) => {
                if (data.code === 200) {
                    this.$message.success("分类修改成功");
                    this.getMenus();
                    this.expandedKey = [this.category.parentCid];
                    this.category.name = "";
                } else {
                    this.$message.error("分类修改失败: " + data.msg);
                }
            });


            console.log("修改分类请求发送完毕");
        },
        allowDrop(draggingNode, dropNode, type) {
            if (type === 'inner') {
                return dropNode.level + this.countMaxDepth(draggingNode.data) <= 3;
            } else if (type === 'prev' || type === 'next') {
                return this.countMaxDepth(draggingNode.data) + dropNode.level - 1 <= 3;
            }
            return false;
        },
        countMaxDepth(node) {
            if (!node.children || node.children.length === 0) {
                return 1;
            }
            let depths = node.children.map(child => this.countMaxDepth(child));
            return 1 + Math.max(...depths);
        },
        handleDrop(draggingNode, dropNode, dropType) {
            this.updateNodes = [];

            let pCid = 0;
            let siblings = null;

            // --- 确定父节点和兄弟列表 ---
            if (dropType === "inner") {
                pCid = dropNode.data.catId;
                siblings = dropNode.childNodes;
            } else {
                pCid = dropNode.parent.data ? dropNode.parent.data.catId : 0;
                siblings = dropNode.parent ? dropNode.parent.childNodes : [];
            }

            // --- 重新排序兄弟节点 ---
            for (let i = 0; i < siblings.length; i++) {
                // 如果是当前正在拖拽的那个节点
                if (siblings[i].data.catId === draggingNode.data.catId) {

                    // 计算层级变化：当前节点的最新层级
                    let level = 0;
                    if (dropType === "inner") {
                        // 如果是拖入内部，层级 = 目标节点层级 + 1
                        level = dropNode.level + 1;
                    } else {
                        // 如果是前后拖拽，层级 = 目标节点层级
                        level = dropNode.level;
                    }

                    // 判断层级是否真的变了（原层级 vs 新层级）
                    if (draggingNode.data.catLevel !== level) {
                        // 如果层级变了，需要更新当前节点 + 所有子孙节点的层级
                        this.updateChildNodeLevel(siblings[i], level);
                    }

                    // 记录当前节点的更新信息
                    this.updateNodes.push({
                        catId: siblings[i].data.catId,
                        parentCid: pCid,
                        sort: i,
                        catLevel: level
                    });
                } else {
                    // 其他兄弟节点，只需要更新排序
                    this.updateNodes.push({
                        catId: siblings[i].data.catId,
                        sort: i
                    });
                }
            }

            // 打印看看结果
            console.log("最终需要发给后端的更新数据:", this.updateNodes);

            // 发送请求给后端
            this.$http({
                url: this.$http.adornUrl('/product/category/update/batch'),
                method: 'put',
                data: this.$http.adornData(this.updateNodes, false)
            }).then(({ data }) => {
                if (data.code === 200) {
                    this.$message.success("分类排序更新成功");
                    this.getMenus();
                    this.expandedKey = [pCid];
                } else {
                    this.$message.error("分类排序更新失败: " + data.msg);
                }
            });

        },

        // --- 递归更新子节点层级 ---
        updateChildNodeLevel(node, newLevel) {
            // 如果没有子节点，直接结束
            if (!node.childNodes || node.childNodes.length === 0) {
                return;
            }

            // 遍历所有子节点
            for (let i = 0; i < node.childNodes.length; i++) {
                let childNode = node.childNodes[i];

                // 子节点的层级 = 父节点层级 + 1
                let childNewLevel = newLevel + 1;

                // 将需要修改的数据 push 进 updateNodes
                this.updateNodes.push({
                    catId: childNode.data.catId,
                    catLevel: childNewLevel
                });

                // 递归处理子节点的子节点
                this.updateChildNodeLevel(childNode, childNewLevel);
            }
        },
        batchDelete() {
            let keys = this.$refs.menuTree.getCheckedKeys();

            if (keys.length === 0) {
                this.$message.warning("请先选择要删除的分类");
                return;
            }

            this.$confirm(`是否删除选中的【${keys.length}】个分类?`, '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                console.log("批量删除分类，选中的分类ID列表:", keys);

                this.$http({
                    url: this.$http.adornUrl('/product/category/delete'),
                    method: 'delete',
                    data: this.$http.adornData(keys, false)
                }).then(({ data }) => {
                    if (data.code === 200) {
                        this.$message.success("分类删除成功");
                        this.getMenus();
                        // 删除后不需要展开特定的节点，或者你可以选择展开父节点
                        this.expandedKey = [];
                    } else {
                        this.$message.error("分类删除失败: " + data.msg);
                    }
                });
            }).catch(() => {
                // 取消删除，不做任何操作
            });
        }
    },
    created() {
        this.getMenus();
    }
};
</script>

<style></style>