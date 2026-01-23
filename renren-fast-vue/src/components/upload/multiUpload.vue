<template>
  <div>
    <el-upload action="http://cloudmall-project.oss-cn-beijing.aliyuncs.com" :data="dataObj" :list-type="listType"
      :file-list="fileList" :before-upload="beforeUpload" :on-remove="handleRemove" :on-success="handleUploadSuccess"
      :on-preview="handlePreview" :limit="maxCount" :on-exceed="handleExceed" :show-file-list="showFile">
      <i class="el-icon-plus"></i>
    </el-upload>
    <el-dialog :visible.sync="dialogVisible">
      <img width="100%" :src="dialogImageUrl" alt />
    </el-dialog>
  </div>
</template>
<script>
import { policy } from "./policy";
import { getUUID } from '@/utils'

export default {
  name: "multiUpload",
  props: {
    //图片属性数组
    value: Array,
    //最大上传图片数量
    maxCount: {
      type: Number,
      default: 30
    },
    listType: {
      type: String,
      default: "picture-card"
    },
    showFile: {
      type: Boolean,
      default: true
    }
  },
  data() {
    return {

      dataObj: {
        policy: '',
        'x-oss-signature': '',
        key: '',
        dir: '',
        host: '',
        'x-oss-security-token': '',
        'x-oss-date': '',
        'x-oss-credential': '',
        'x-oss-signature-version': 'OSS4-HMAC-SHA256',
        'success_action_status': '200'
      },
      dialogVisible: false,
      dialogImageUrl: null
    };
  },
  computed: {
    fileList() {
      let fileList = [];

      if (this.value && this.value.length > 0) {
        for (let i = 0; i < this.value.length; i++) {
          fileList.push({ url: this.value[i] });
        }
      }
      return fileList;
    }
  },
  mounted() { },
  methods: {

    emitInput(fileList) {
      let value = [];
      for (let i = 0; i < fileList.length; i++) {
        value.push(fileList[i].url);
      }
      this.$emit("input", value);
    },
    handleRemove(file, fileList) {
      this.emitInput(fileList);
    },
    handlePreview(file) {
      this.dialogVisible = true;
      this.dialogImageUrl = file.url;
    },
    beforeUpload(file) {
      let _self = this;
      return new Promise((resolve, reject) => {
        policy()
          .then(response => {
            const data = response.data;

            _self.dataObj.policy = data.policy;

            _self.dataObj['x-oss-signature'] = data.signature;
            _self.dataObj.dir = data.dir;

            let rawHost = data.host;
            if (rawHost.includes('sts')) {
              _self.dataObj.host = rawHost.replace('.sts.', '.oss-');
            } else {
              _self.dataObj.host = rawHost;
            }

            _self.dataObj['x-oss-security-token'] = data.securityToken;
            _self.dataObj['x-oss-credential'] = data.xossCredential;
            _self.dataObj['x-oss-date'] = data.xossDate;
            _self.dataObj['x-oss-signature-version'] = data.xossSignatureVersion;

            _self.dataObj.key = data.dir + getUUID() + '_${filename}';

            resolve(true);
          })
          .catch(err => {
            console.error("获取签名失败...", err);
            reject(false);
          });
      });
    },
    handleUploadSuccess(res, file) {
      const finalUrl = this.dataObj.host + '/' + this.dataObj.key.replace("${filename}", file.name);

      let newValue = this.value ? [...this.value] : [];
      newValue.push(finalUrl);

      this.$emit("input", newValue);
    },
    handleExceed(files, fileList) {
      this.$message({
        message: "最多只能上传" + this.maxCount + "张图片",
        type: "warning",
        duration: 1000
      });
    }
  }
};
</script>
<style></style>
